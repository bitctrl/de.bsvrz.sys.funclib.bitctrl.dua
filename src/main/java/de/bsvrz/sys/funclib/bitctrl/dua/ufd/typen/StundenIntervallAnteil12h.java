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

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen;

import java.util.HashMap;
import java.util.Map;

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Korrespondiert mit <code>att.stundenIntervallAnteile12h</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class StundenIntervallAnteil12h extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, StundenIntervallAnteil12h> werteBereich = new HashMap<>();

	/**
	 * Alle wirklichen Enumerationswerte
	 *
	 * Wert: STUNDEN_1.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_1 = new StundenIntervallAnteil12h("1 Stunde", 1,
			Constants.MILLIS_PER_HOUR);

	/**
	 * Wert: STUNDEN_2.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_2 = new StundenIntervallAnteil12h("2 Stunden", 2,
			2 * Constants.MILLIS_PER_HOUR);

	/**
	 * Wert: STUNDEN_3.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_3 = new StundenIntervallAnteil12h("3 Stunden", 3,
			3 * Constants.MILLIS_PER_HOUR);

	/**
	 * Wert: STUNDEN_4.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_4 = new StundenIntervallAnteil12h("4 Stunden", 4,
			4 * Constants.MILLIS_PER_HOUR);

	/**
	 * Wert: STUNDEN_6.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_6 = new StundenIntervallAnteil12h("6 Stunden", 6,
			6 * Constants.MILLIS_PER_HOUR);

	/**
	 * Wert: STUNDEN_8.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_8 = new StundenIntervallAnteil12h("8 Stunden", 8,
			8 * Constants.MILLIS_PER_HOUR);

	/**
	 * Wert: STUNDEN_12.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_12 = new StundenIntervallAnteil12h("12 Stunden", 12,
			12 * Constants.MILLIS_PER_HOUR);

	/**
	 * der hier definierte Zeitbereich in Millisekunden.
	 */
	private long millis = -1;

	/**
	 * Interner Konstruktor.
	 *
	 * @param name
	 *            der Name des Zustandes
	 * @param code
	 *            der Kode
	 * @param millis
	 *            der hier definierte Zeitbereich in Millisekunden
	 */
	private StundenIntervallAnteil12h(final String name, final int code, final long millis) {
		super(code, name);
		this.millis = millis;
		StundenIntervallAnteil12h.werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 */
	public static StundenIntervallAnteil12h getZustand(final int code) {
		return StundenIntervallAnteil12h.werteBereich.get(code);
	}

	/**
	 * Erfragt den hier definierten Zeitbereich in Millisekunden.
	 *
	 * @return der hier definierte Zeitbereich in Millisekunden
	 */
	public long getMillis() {
		return millis;
	}

}
