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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Ein Verkehrsnetz im Sinne der DUA.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DuaVerkehrsNetz {

	@Deprecated
	private static DuaVerkehrsNetz defaultInstance;
	
	/**
	 * die Datenverteilerverbindung mit dem das Netz initialisiert wurde. Es
	 * wird nur eine Datenverteilerverbindung unterstützt.
	 */
	private ClientDavInterface usedDav;
	
	private final Map<SystemObject, FahrStreifen> fahrStreifen = new LinkedHashMap<>();
	private final Map<SystemObject, MessQuerschnitt> messQuerSchnitte = new LinkedHashMap<>();
	private final Map<SystemObject, MessQuerschnittVirtuell> messQuerSchnitteVirtuell = new LinkedHashMap<>();
	private final Map<SystemObject, MessStelle> messStellen = new LinkedHashMap<>();
	private final Map<SystemObject, MessStellenGruppe> messStellenGruppen = new LinkedHashMap<>();


	/**
	 * erzeugt ein {@link DuaVerkehrsNetz} auf Basis der übergebenen
	 * Datenverteilerverbindung.
	 * 
	 * Das verwendete Modell kann durch die Übergabe einer Menge von
	 * Konfigurationsbereichen eingeschränkt werden. Ist das übergebene Feld
	 * leer oder wird <code>null</code> übergeben, erfolgt keine Beschränkung.
	 * 
	 * @param dav
	 *            die verwendete Datenverteilerverbindung
	 * @param kbs
	 *            die optionale Liste der KB auf die das Netz beschränkt werden
	 *            soll
	 * 
	 * @throws DUAInitialisierungsException die Initialisierung des Netzes ist fehlgeschlagen.
	 */
	public DuaVerkehrsNetz(ClientDavInterface dav, final ConfigurationArea[] kbs) throws DUAInitialisierungsException {
		defaultInstance = this;
		usedDav = dav;
		fahrStreifen.putAll(FahrStreifen.einlesen(this, dav, kbs));
		messQuerSchnitte.putAll(MessQuerschnitt.einlesen(this, dav, kbs));
		messQuerSchnitteVirtuell.putAll(MessQuerschnittVirtuell.einlesen(this, dav, kbs));
		messStellen.putAll(MessStelle.einlesen(this, dav, kbs));
		ermittleErsatzUndNachbarFS();
		messStellenGruppen.putAll(MessStellenGruppe.einlesen(this, dav, kbs));
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
	 * 
	 * @deprecated eine Applikation sollte eine Instanz des
	 *             {@link DuaVerkehrsNetz} halten. Damit wäre die Relation von
	 *             Datenverteilerverbindung (DatenModell) und implizit
	 *             zwischengespeicherten Modellobjekten eindeutig.
	 */
	@Deprecated
	public static synchronized void initialisiere(final ClientDavInterface dav) throws DUAInitialisierungsException {
		if ((defaultInstance != null) && (dav == defaultInstance.usedDav)) {
			Debug.getLogger().warning("Das DUA-Verkehrsnetz wurde bereits initialisiert");
		} else {
			defaultInstance = new DuaVerkehrsNetz(dav, null);
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
	 *
	 * @deprecated eine Applikation sollte eine Instanz des
	 *             {@link DuaVerkehrsNetz} halten. Damit wäre die Relation von
	 *             Datenverteilerverbindung (DatenModell) und implizit
	 *             zwischengespeicherten Modellobjekten eindeutig.
	 */
	@Deprecated
	public static synchronized void initialisiere(final ClientDavInterface dav, final ConfigurationArea[] kbs)
			throws DUAInitialisierungsException {
		if ((defaultInstance != null) && (dav == defaultInstance.usedDav)) {
			Debug.getLogger().warning("Das DUA-Verkehrsnetz wurde bereits initialisiert");
		} else {
			defaultInstance = new DuaVerkehrsNetz(dav, kbs);
		}
	}

	@Deprecated
	static DuaVerkehrsNetz getDefaultInstance() {
		return defaultInstance;
	}

	/**
	 * Ermittelt für alle Fahrstreifen die Nachbar- bzw. Ersatzfahrstreifen, so
	 * diese nicht explizit versorgt sind und trägt sie an den entsprechenden
	 * Fahrtreifen ein
	 */
	private void ermittleErsatzUndNachbarFS() {

		for (final MessQuerschnitt mq : getAlleMessQuerSchnitte()) {
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
	
	public Collection<FahrStreifen> getAlleFahrStreifen() {
		return Collections.unmodifiableCollection(fahrStreifen.values());
	}

	public FahrStreifen getFahrStreifen(SystemObject systemObject) {
		return fahrStreifen.get(systemObject);
	}

	public Collection<MessQuerschnitt> getAlleMessQuerSchnitte() {
		return Collections.unmodifiableCollection(messQuerSchnitte.values());
	}

	public MessQuerschnitt getMessQuerSchnitt(SystemObject systemObject) {
		return messQuerSchnitte.get(systemObject);
	}

	public Collection<MessQuerschnittVirtuell> getAlleMessQuerSchnitteVirtuell() {
		return Collections.unmodifiableCollection(messQuerSchnitteVirtuell.values());
	}

	public MessQuerschnittVirtuell getMessQuerSchnittVirtuell(SystemObject systemObject) {
		return messQuerSchnitteVirtuell.get(systemObject);
	}

	public MessQuerschnittAllgemein getMessQuerSchnittAllgemein(SystemObject mqaObjekt) {
		MessQuerschnittAllgemein result = getMessQuerSchnitt(mqaObjekt);
		if( result != null) {
			return result;
		}
		
		return getMessQuerSchnittVirtuell(mqaObjekt);
	}

	
	public Collection<MessStelle> getAlleMessStellen() {
		return Collections.unmodifiableCollection(messStellen.values());
	}

	public MessStelle getMessStelle(SystemObject systemObject) {
		return messStellen.get(systemObject);
	}

	public Collection<MessStellenGruppe> getAlleMessStellenGruppen() {
		return Collections.unmodifiableCollection(messStellenGruppen.values());
	}

	public MessStellenGruppe getMessStellenGruppe(SystemObject systemObject) {
		return messStellenGruppen.get(systemObject);
	}

}
