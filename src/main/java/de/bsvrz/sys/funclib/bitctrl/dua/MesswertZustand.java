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

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Korrespondiert mit dem Zustandsraum eines Messwertes.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class MesswertZustand extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, MesswertZustand> werteBereich = new HashMap<>();

	/**
	 * Daten sind nicht ermittelbar (ist KEIN Fehler). Wird gesetzt, wenn der
	 * entsprechende Wert nicht ermittelbar ist und kein Interpolation sinnvoll
	 * möglich ist (z.B. ist die Geschwindigkeit nicht ermittelbar, wenn kein
	 * Fahrzeug erfasst wurde).
	 */
	public static final MesswertZustand NICHT_ERMITTELBAR = new MesswertZustand("nicht ermittelbar", -1);

	/**
	 * Daten sind fehlerhaft. Wird gesetzt, wenn die Daten als fehlerhaft
	 * erkannt wurden
	 */
	public static final MesswertZustand FEHLERHAFT = new MesswertZustand("fehlerhaft", -2);

	/**
	 * Daten nicht ermittelbar, da bereits Basiswerte fehlerhaft. Wird gesetzt,
	 * wenn Daten, die zur Berechnung dieses Werts notwendig sind, bereits als
	 * fehlerhaft gekennzeichnet sind, oder wenn die Berechnung aus anderen
	 * Gründen (z.B. Nenner = 0 in der Berechnungsformel) nicht möglich war.
	 */
	public static final MesswertZustand FEHLERHAFT_BZW_NICHT_ERMITTELBAR = new MesswertZustand(
			"nicht ermittelbar/fehlerhaft", -3);

	/**
	 * Interner Konstruktor.
	 *
	 * @param code
	 *            der Code
	 * @param name
	 *            die Bezeichnung
	 */
	private MesswertZustand(final String name, final int code) {
		super(code, name);
		MesswertZustand.werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 */
	public static MesswertZustand getZustand(final int code) {
		return MesswertZustand.werteBereich.get(code);
	}

}
