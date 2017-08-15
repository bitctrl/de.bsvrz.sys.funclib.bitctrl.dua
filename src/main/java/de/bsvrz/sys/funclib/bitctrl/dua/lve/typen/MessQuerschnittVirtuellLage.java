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

package de.bsvrz.sys.funclib.bitctrl.dua.lve.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.messQuerschnittVirtuellLage</code> beschriebenen Werte zur
 * Verfügung gestellt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 **/
public final class MessQuerschnittVirtuellLage extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, MessQuerschnittVirtuellLage> werteBereich = new HashMap<>();

	/**
	 * Wert <code>Vor</code>.
	 */
	public static final MessQuerschnittVirtuellLage VOR = new MessQuerschnittVirtuellLage("Vor", 0);

	/**
	 * Wert <code>Mitte</code>.
	 */
	public static final MessQuerschnittVirtuellLage MITTE = new MessQuerschnittVirtuellLage("Mitte", 1);

	/**
	 * Wert <code>Nach</code>.
	 */
	public static final MessQuerschnittVirtuellLage NACH = new MessQuerschnittVirtuellLage("Nach", 2);

	/**
	 * Wert <code>Ausfahrt</code>.
	 */
	public static final MessQuerschnittVirtuellLage AUSFAHRT = new MessQuerschnittVirtuellLage("Ausfahrt", 3);

	/**
	 * Wert <code>Einfahrt</code>.
	 */
	public static final MessQuerschnittVirtuellLage EINFAHRT = new MessQuerschnittVirtuellLage("Einfahrt", 4);

	/**
	 * Interner Konstruktor.
	 *
	 * @param name
	 *            der Name des Typen
	 * @param code
	 *            dessen Kode
	 */
	private MessQuerschnittVirtuellLage(final String name, final int code) {
		super(code, name);
		MessQuerschnittVirtuellLage.werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code
	 */
	public static MessQuerschnittVirtuellLage getZustand(final int code) {
		return MessQuerschnittVirtuellLage.werteBereich.get(code);
	}
}
