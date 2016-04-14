/*
 * Allgemeine Funktionen für das Segment DuA
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Ein Verkehrsnetz im Sinne der DUA.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DuaVerkehrsNetz {

	/**
	 * Flag: Wurde das statische DUA-Verkehrsnetz bereits initialisiert?
	 */
	private static boolean initialisiert;

	/**
	 * Standardkonstruktor.
	 */
	protected DuaVerkehrsNetz() {

	}

	/**
	 * Initialisiert das gesamte Verkehrs-Netz aus Sicht der DUA<br>
	 * Nach Aufruf dieser Methode sind insbesondere die Fahrtreifen mit den
	 * Informationen zu ihren Ersatz und Nachbarfahrstreifen initialisiert.
	 *
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @throws DUAInitialisierungsException
	 *             wenn es Probleme geben sollte, die die Initialisierung des
	 *             Netzes (im Sinne der DUA) nicht möglich machen
	 */
	public static synchronized void initialisiere(final ClientDavInterface dav) throws DUAInitialisierungsException {
		if (DuaVerkehrsNetz.initialisiert) {
			Debug.getLogger().warning("Das DUA-Verkehrsnetz wurde bereits initialisiert");
		} else {
			DuaVerkehrsNetz.initialisiert = true;
			FahrStreifen.initialisiere(dav);
			MessQuerschnitt.initialisiere(dav);
			MessQuerschnittVirtuell.initialisiere(dav);
			MessStelle.initialisiere(dav);
			DuaVerkehrsNetz.ermittleErsatzUndNachbarFS();
			MessStellenGruppe.initialisiere(dav);
		}
	}

	/**
	 * Initialisiert das gesamte Verkehrs-Netz aus Sicht der DUA<br>
	 * Nach Aufruf dieser Methode sind insbesondere die Fahrtreifen mit den
	 * Informationen zu ihren Ersatz und Nachbarfahrstreifen initialisiert.
	 *
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @param kbs
	 *            Menge der zu betrachtenden Konfigurationsbereiche
	 * @throws DUAInitialisierungsException
	 *             wenn es Probleme geben sollte, die die Initialisierung des
	 *             Netzes (im Sinne der DUA) nicht möglich machen
	 */
	public static synchronized void initialisiere(final ClientDavInterface dav, final ConfigurationArea[] kbs)
			throws DUAInitialisierungsException {
		if (DuaVerkehrsNetz.initialisiert) {
			Debug.getLogger().warning("Das DUA-Verkehrsnetz wurde bereits initialisiert");
		} else {
			DuaVerkehrsNetz.initialisiert = true;
			FahrStreifen.initialisiere(dav, kbs);
			MessQuerschnitt.initialisiere(dav, kbs);
			MessQuerschnittVirtuell.initialisiere(dav, kbs);
			MessStelle.initialisiere(dav, kbs);
			DuaVerkehrsNetz.ermittleErsatzUndNachbarFS();
			MessStellenGruppe.initialisiere(dav, kbs);
		}
	}

	/**
	 * Ermittelt für alle Fahrstreifen die Nachbar- bzw. Ersatzfahrstreifen, so
	 * diese nicht explizit versorgt sind und trägt sie an den entsprechenden
	 * Fahrtreifen ein
	 */
	private static void ermittleErsatzUndNachbarFS() {

		for (final MessQuerschnitt mq : MessQuerschnitt.getInstanzen()) {
			for (final FahrStreifen fs : mq.getFahrStreifen()) {

				if (fs.getNachbarFahrStreifen() == null) {
					final FahrStreifen nachbar = mq.getNachbarVon(fs);
					if (nachbar != null) {
						fs.setNachbarFahrStreifen(nachbar.getSystemObject());
					}
				}

				if (fs.getNachbarFahrStreifen() == null) {
					Debug.getLogger()
					.warning("Für Fahrstreifen " + fs + " kann " + "kein Nachbarfahrstreifen ermittelt werden");
				}

				if (fs.getErsatzFahrStreifen() == null) {
					/**
					 * Ersatzfahrstreifen ist Fahrstreifen desselben Typs an
					 * Ersatzmessstelle
					 */
					final MessQuerschnittAllgemein ersatzQuerschnitt = mq.getErsatzMessquerSchnitt();
					if (ersatzQuerschnitt != null) {
						final List<FahrStreifen> ersatzFahstreifen = new ArrayList<>();
						for (final FahrStreifen fsAnErsatzQuerschnitt : ersatzQuerschnitt.getFahrStreifen()) {
							if (fsAnErsatzQuerschnitt.getLage().equals(fs.getLage())) {
								ersatzFahstreifen.add(fsAnErsatzQuerschnitt);
							}
						}

						if (ersatzFahstreifen.size() > 0) {
							if (ersatzFahstreifen.size() > 1) {
								Debug.getLogger().warning("Für Fahrstreifen " + fs + " sind mehrere"
										+ " Ersatzfahrstreifen ermittelbar." + " Wähle: " + ersatzFahstreifen.get(0));
							}
							fs.setErsatzFahrStreifen(ersatzFahstreifen.get(0).getSystemObject());
						}
					}
				}

				if (fs.getErsatzFahrStreifen() == null) {
					Debug.getLogger()
					.warning("Für Fahrstreifen " + fs + " kann " + "kein Ersatzfahrstreifen ermittelt werden");
				}
			}
		}

	}
}
