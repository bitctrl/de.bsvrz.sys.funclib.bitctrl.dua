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

package de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.bitctrl.daf.BetriebsmeldungIdKonverter;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;

/**
 * Abstrakte Implementation einer Schnittstelle zu einem Verwaltungsmodul. Ein
 * Verwaltungsmodul stellt immer den Eintrittspunkt in eine SWE 4.x dar und
 * implementiert daher das Interface <code>StandardApplication</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public interface IVerwaltung extends StandardApplication, ClientReceiverInterface {

	/**
	 * Erfragt die Verbindung zum Datenverteiler.
	 *
	 * @return die Verbindung zum Datenverteiler
	 */
	ClientDavInterface getVerbindung();

	// /**
	// * Sendet eine Betriebsmeldung an die Betriebsmeldungsverwaltung.
	// *
	// * @param id ID der Meldung. Dieses Attribut kann von der Applikation
	// * gesetzt werden, um einen Bezug zu einer vorherigen Meldung
	// herzustellen.
	// * @param typ Typ der Betriebsmeldung (Diese Klasse stellt die beiden
	// Zustände
	// * "System" und "Fach" für Meldungen, die sich auf systemtechnische oder
	// * fachliche Zustände beziehen, bereit)
	// * @param nachrichtenTypErweiterung Erweiterung
	// * @param klasse Klasse der Betriebsmeldung
	// * @param status Gibt den Zustand einer Meldung an
	// * @param nachricht Nachrichtentext der Betriebsmeldung
	// */
	// void sendeBetriebsMeldung(final String id, final MessageType typ,
	// final String nachrichtenTypErweiterung, final MessageGrade klasse,
	// final MessageState status, final String nachricht);

	/**
	 * Über diese Methode soll ein Modul Verwaltung anderen Modulen die Menge
	 * aller zu bearbeitenden Objekte zur Verfügung stellen. Sollte an dieser
	 * Stelle <code>null</code> übergeben werden, so sollten vom fragenden Modul
	 * alle inhaltlich passenden Systemobjekte des
	 * Standardkonfigurationsbereichs zur Bearbeitung angenommen werden.
	 *
	 * @return alle zu bearbeitenden Objekte
	 */
	SystemObject[] getSystemObjekte();

	/**
	 * Erfragt die dem Verwaltungsmodul übergebenen Konfigurationsbereiche.
	 *
	 * @return alle Konfigurationsbereiche, die diesem Verwaltungsmodul
	 *         übergeben wurden.
	 */
	Collection<ConfigurationArea> getKonfigurationsBereiche();

	/**
	 * Erfragt die SWE, für die die dieses Interface implementierende Klasse die
	 * Verwaltung darstellt.
	 *
	 * @return die SWE, für die die dieses Interface implementierende Klasse die
	 *         Verwaltung darstellt
	 */
	SWETyp getSWETyp();

	/**
	 * Erfragt ein Kommandozeilenargument der Applikation.
	 *
	 * @param schluessel
	 *            der Name des Arguments
	 * @return das Kommandozeilenargument des Schluessels oder <code>null</code>
	 *         , wenn das Argument nicht uebergeben wurde
	 */
	String getArgument(final String schluessel);

	/**
	 * Erfragt den Umsetzer fuer die Betriebsmeldungs-Id.
	 *
	 * @return der Umsetzer fuer die Betriebsmeldungs-Id.
	 */
	BetriebsmeldungIdKonverter getBmvIdKonverter();

}
