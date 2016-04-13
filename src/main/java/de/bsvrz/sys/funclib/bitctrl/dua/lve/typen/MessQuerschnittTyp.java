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
 * <code>att.messQuerschnittTyp</code> beschriebenen Werte zur Verfügung
 * gestellt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 **/
public final class MessQuerschnittTyp extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, MessQuerschnittTyp> werteBereich = new HashMap<>();

	/**
	 * Wert <code>SonstigeFahrbahn</code>.
	 */
	public static final MessQuerschnittTyp SONSTIGE = new MessQuerschnittTyp("SonstigeFahrbahn", 0);

	/**
	 * Wert <code>HauptFahrbahn</code>.
	 */
	public static final MessQuerschnittTyp HAUPT = new MessQuerschnittTyp("HauptFahrbahn", 1);

	/**
	 * Wert <code>NebenFahrbahn</code>.
	 */
	public static final MessQuerschnittTyp NEBEN = new MessQuerschnittTyp("NebenFahrbahn", 2);

	/**
	 * Wert <code>Einfahrt</code>.
	 */
	public static final MessQuerschnittTyp EINFAHRT = new MessQuerschnittTyp("Einfahrt", 3);

	/**
	 * Wert <code>Ausfahrt</code>.
	 */
	public static final MessQuerschnittTyp AUSFAHRT = new MessQuerschnittTyp("Ausfahrt", 4);

	/**
	 * Interner Konstruktor.
	 *
	 * @param name
	 *            der Name des Typen
	 * @param code
	 *            dessen Kode
	 */
	private MessQuerschnittTyp(final String name, final int code) {
		super(code, name);
		MessQuerschnittTyp.werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code
	 */
	public static MessQuerschnittTyp getZustand(final int code) {
		return MessQuerschnittTyp.werteBereich.get(code);
	}
}
