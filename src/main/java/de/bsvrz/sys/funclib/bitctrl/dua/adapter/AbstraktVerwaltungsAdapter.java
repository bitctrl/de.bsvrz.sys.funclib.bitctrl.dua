/*
 * Allgemeine Funktionen für das Segment DuA
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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.BetriebsmeldungIdKonverter;
import de.bsvrz.sys.funclib.bitctrl.daf.DefaultBetriebsMeldungsIdKonverter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
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
	private final Set<SystemObject> objekte = new LinkedHashSet<>();

	/**
	 * Verbindung zum Datenverteiler.
	 */
	private ClientDavInterface verbindung;

	/**
	 * die Argumente der Kommandozeile.
	 */
	private final ArrayList<String> komArgumente = new ArrayList<>();

	/**
	 * Die Konfigurationsbreiche, deren Objekte bearbeitet werden sollen.
	 */
	private Collection<ConfigurationArea> kBereiche = new HashSet<>();

	/**
	 * Umsetzer fuer die Betriebsmeldung in eine ID.
	 */
	private final BetriebsmeldungIdKonverter bmvKonverter = new DefaultBetriebsMeldungsIdKonverter();

	@Override
	public String getArgument(final String schluessel) {
		return DUAUtensilien.getArgument(schluessel, komArgumente);
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

	/**
	 * liefert die Liste der Kommunikations-Argumente.
	 *
	 * @return die Liste
	 */
	public ArrayList<String> getKomArgumente() {
		return komArgumente;
	}

	@Override
	public final Collection<ConfigurationArea> getKonfigurationsBereiche() {
		return kBereiche;
	}

	@Override
	public final Collection<SystemObject> getSystemObjekte() {
		return Collections.unmodifiableCollection(objekte);
	}

	/**
	 * fügt der Liste der zu verwaltenden Objekte die übergebenen Objekte hinzu.
	 *
	 * @param newObjects
	 *            die neuen Objekte
	 */
	public void addSystemObjekte(final Collection<SystemObject> newObjects) {
		objekte.addAll(newObjects);
	}

	/**
	 * fügt der Liste der zu verwaltenden Objekte die übergebenen Objekte hinzu
	 * und löscht alle vorher eingetragenen.
	 *
	 * @param newObjects
	 *            die neuen Objekte
	 */
	public void setSystemObjekte(final Collection<SystemObject> newObjects) {
		objekte.clear();
		objekte.addAll(newObjects);
	}

	@Override
	public final ClientDavInterface getVerbindung() {
		return verbindung;
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

	@Override
	public void initialize(final ClientDavInterface dieVerbindung) throws Exception {
		try {

			verbindung = dieVerbindung;
			if (komArgumente != null) {
				kBereiche = DUAUtensilien.getKonfigurationsBereicheAlsObjekte(verbindung,
						DUAUtensilien.getArgument(DUAKonstanten.ARG_KONFIGURATIONS_BEREICHS_PID, komArgumente));
			} else {
				throw new DUAInitialisierungsException("Es wurden keine" + " Kommandozeilenargumente übergeben");
			}

			/**
			 * Initialisiere das eigentliche Verwaltungsmodul
			 */
			initialisiere();

			Debug.getLogger().config(toString());

		} catch (final DUAInitialisierungsException ex) {
			final String fehler = "Initialisierung der Applikation " + getSWETyp().toString() + " fehlgeschlagen";
			Debug.getLogger().error(fehler, ex);
			ex.printStackTrace();

			if (verbindung != null) {
				verbindung.disconnect(true, fehler);
			} else {
				System.exit(0);
			}
		}
	}

	@Override
	public void parseArguments(final ArgumentList argumente) throws Exception {

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(final Thread t, final Throwable e) {
				Debug.getLogger().error("Applikation wird wegen" + " unerwartetem Fehler beendet", e);
				e.printStackTrace();
				Runtime.getRuntime().exit(-1);
			}
		});

		for (final String s : argumente.getArgumentStrings()) {
			if (s != null) {
				addKomArgument(s);
			}
		}

		argumente.fetchUnusedArguments();
	}

	protected void clearKomArguments() {
		komArgumente.clear();
	}

	protected boolean addKomArgument(final String s) {
		return komArgumente.add(s);
	}

	@Override
	public String toString() {
		final String s = "SWE: " + getSWETyp() + "\n";

		String dummy = "---keine Konfigurationsbereiche angegeben---\n";
		if (kBereiche.size() > 0) {
			dummy = Constants.EMPTY_STRING;
			for (final ConfigurationArea kb : kBereiche) {
				dummy += kb + "\n";
			}
		}

		return s + "Konfigurationsbereiche:\n" + dummy;
	}

}
