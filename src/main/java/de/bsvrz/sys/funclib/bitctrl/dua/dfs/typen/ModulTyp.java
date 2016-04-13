/*
 * Allgemeine Funktionen f�r das Segment DuA
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * �ber diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.modulTyp</code> beschriebenen Werte zur Verf�gung gestellt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class ModulTyp extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, ModulTyp> werteBereich = new HashMap<>();

	/**
	 * Wert <code>PlPr�fungFormal</code>.
	 */
	public static final ModulTyp PL_PRUEFUNG_FORMAL = new ModulTyp("PlPr�fungFormal", 1);

	/**
	 * Wert <code>PlPr�fungLogischUFD</code>.
	 */
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_UFD = new ModulTyp("PlPr�fungLogischUFD", 2);

	/**
	 * Wert <code>PlPr�fungLogischWZG</code>.
	 */
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_WZG = new ModulTyp("PlPr�fungLogischWZG", 3);

	/**
	 * Wert <code>PlPr�fungMesswertErsetzungLVE</code>.
	 */
	public static final ModulTyp MESSWERTERSETZUNG_LVE = new ModulTyp("PlPr�fungMesswertErsetzungLVE", 4);

	/**
	 * Wert <code>PlPr�fungMesswertErsetzungUFD</code>.
	 */
	public static final ModulTyp MESSWERTERSETZUNG_UFD = new ModulTyp("PlPr�fungMesswertErsetzungUFD", 5);

	/**
	 * Wert <code>PlPr�fungLangZeitUFD</code>.
	 */
	public static final ModulTyp PL_PRUEFUNG_LANGZEIT_UFD = new ModulTyp("PlPr�fungLangZeitUFD", 6);

	/**
	 * Interner Konstruktor.
	 *
	 * @param name
	 *            der Name des Zustandes
	 * @param code
	 *            der Kode
	 */
	private ModulTyp(final String name, final int code) {
		super(code, name);
		ModulTyp.werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem �bergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem �bergebenen Code.
	 */
	public static ModulTyp getZustand(final int code) {
		return ModulTyp.werteBereich.get(code);
	}

}
