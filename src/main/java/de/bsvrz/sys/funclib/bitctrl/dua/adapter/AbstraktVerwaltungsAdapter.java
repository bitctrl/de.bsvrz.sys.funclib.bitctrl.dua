/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
 * Copyright 2016 by Kappich Systemberatung Aachen
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.BetriebsmeldungIdKonverter;
import de.bsvrz.sys.funclib.bitctrl.daf.DefaultBetriebsMeldungsIdKonverter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DatenFlussSteuerungsVersorger;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Adapterklasse für Verwaltungsmodule.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class AbstraktVerwaltungsAdapter implements IVerwaltung {

	/**
	 * Die Objekte, die bearbeitet werden sollen.
	 */
	protected SystemObject[] objekte;

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected ClientDavInterface verbindung;

	/**
	 * die Argumente der Kommandozeile.
	 */
	protected ArrayList<String> komArgumente = new ArrayList<String>();

	/**
	 * Die Konfigurationsbreiche, deren Objekte bearbeitet werden sollen.
	 */
	private Collection<ConfigurationArea> kBereiche = new HashSet<ConfigurationArea>();

	/**
	 * Verbindung zur Datenflusssteuerung.
	 */
	protected DatenFlussSteuerungsVersorger dfsHilfe;

	/**
	 * Umsetzer fuer die Betriebsmeldung in eine ID.
	 */
	protected BetriebsmeldungIdKonverter bmvKonverter = new DefaultBetriebsMeldungsIdKonverter();

	@Override
	public final Collection<ConfigurationArea> getKonfigurationsBereiche() {
		return kBereiche;
	}

	@Override
	public final SystemObject[] getSystemObjekte() {
		return objekte;
	}

	@Override
	public final ClientDavInterface getVerbindung() {
		return verbindung;
	}

	@Override
	public void initialize(final ClientDavInterface dieVerbindung)
			throws Exception {
		try {

			verbindung = dieVerbindung;
			if (komArgumente != null) {
				kBereiche = DUAUtensilien.getKonfigurationsBereicheAlsObjekte(
						verbindung,
						DUAUtensilien.getArgument(
								DUAKonstanten.ARG_KONFIGURATIONS_BEREICHS_PID,
								komArgumente));
				dfsHilfe = DatenFlussSteuerungsVersorger.getInstanz(this);
			} else {
				throw new DUAInitialisierungsException("Es wurden keine" + //$NON-NLS-1$
						" Kommandozeilenargumente übergeben"); //$NON-NLS-1$
			}

			/**
			 * Initialisiere das eigentliche Verwaltungsmodul
			 */
			initialisiere();

			Debug.getLogger().config(toString());

		} catch (final DUAInitialisierungsException ex) {
			final String fehler = "Initialisierung der Applikation " + //$NON-NLS-1$
					getSWETyp().toString() + " fehlgeschlagen"; //$NON-NLS-1$
			Debug.getLogger().error(fehler, ex);
			ex.printStackTrace();

			if (verbindung != null) {
				verbindung.disconnect(true, fehler);
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * Erfragt den Umsetzer fuer die Betriebsmeldungs-Id.
	 *
	 * @return der Umsetzer fuer die Betriebsmeldungs-Id.
	 */
	@Override
	public BetriebsmeldungIdKonverter getBmvIdKonverter() {
		return bmvKonverter;
	}

	@Override
	public void parseArguments(final ArgumentList argumente) throws Exception {

		Thread.setDefaultUncaughtExceptionHandler(
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(final Thread t,
							final Throwable e) {
						Debug.getLogger().error("Applikation wird wegen" + //$NON-NLS-1$
								" unerwartetem Fehler beendet", e); //$NON-NLS-1$
						e.printStackTrace();
						Runtime.getRuntime().exit(-1);
					}
				});

		for (final String s : argumente.getArgumentStrings()) {
			if (s != null) {
				komArgumente.add(s);
			}
		}

		argumente.fetchUnusedArguments();
	}

	@Override
	public String toString() {
		final String s = "SWE: " + getSWETyp() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

		String dummy = "---keine Konfigurationsbereiche angegeben---\n"; //$NON-NLS-1$
		if (kBereiche.size() > 0) {
			dummy = Constants.EMPTY_STRING;
			for (final ConfigurationArea kb : kBereiche) {
				dummy += kb + "\n"; //$NON-NLS-1$
			}
		}

		return s + "Konfigurationsbereiche:\n" + dummy; //$NON-NLS-1$
	}

	@Override
	public String getArgument(final String schluessel) {
		return DUAUtensilien.getArgument(schluessel, komArgumente);
	}

	/**
	 * Diese Methode wird zur Initialisierung aufgerufen, <b>nachdem</b> sowohl
	 * die Argumente der Kommandozeile, als auch die Datenverteilerverbindung
	 * übergeben wurden (also nach dem Aufruf der Methoden
	 * <code>parseArguments(..)</code> und <code>initialize(..)</code>).
	 *
	 * @throws DUAInitialisierungsException
	 *             falls es Probleme bei der Initialisierung geben sollte
	 */
	protected abstract void initialisiere() throws DUAInitialisierungsException;

	protected ArrayList<String> getKomArgumente() {
		return komArgumente;
	}

	protected void clearKomArguments() {
		komArgumente.clear();
	}

	protected void addKomArgument(final String argument) {
		komArgumente.add(argument);
	}

	protected void setSystemObjekte(final Collection<SystemObject> newObjects) {
		objekte = newObjects.toArray(new SystemObject[newObjects.size()]);
	}
}
