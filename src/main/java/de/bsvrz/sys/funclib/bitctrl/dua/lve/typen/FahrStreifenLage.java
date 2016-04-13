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
 * <code>att.fahrStreifenLage</code> beschriebenen Werte zur Verfügung gestellt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 **/
public final class FahrStreifenLage extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, FahrStreifenLage> werteBereich = new HashMap<>();

	/**
	 * Wert <code>HFS</code>.
	 */
	public static final FahrStreifenLage HFS = new FahrStreifenLage("HFS", 0);

	/**
	 * Wert <code>1ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS1 = new FahrStreifenLage("1ÜFS", 1);

	/**
	 * Wert <code>2ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS2 = new FahrStreifenLage("2ÜFS", 2);

	/**
	 * Wert <code>3ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS3 = new FahrStreifenLage("3ÜFS", 3);

	/**
	 * Wert <code>4ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS4 = new FahrStreifenLage("4ÜFS", 4);

	/**
	 * Wert <code>5ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS5 = new FahrStreifenLage("5ÜFS", 5);

	/**
	 * Wert <code>6ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS6 = new FahrStreifenLage("6ÜFS", 6);

	/**
	 * Interner Konstruktor.
	 *
	 * @param name
	 *            der Name des Typen
	 * @param code
	 *            dessen Kode
	 */
	private FahrStreifenLage(final String name, final int code) {
		super(code, name);
		FahrStreifenLage.werteBereich.put(code, this);
	}

	/**
	 * Erfragt die Lage des Fahrtreifens links vom Fahstreifen mit dieser Lage
	 * (in Fahrtrichtung).
	 *
	 * @return die Lage des Fahrtreifens links vom Fahstreifen mit dieser Lage
	 *         oder <code>null</code>, wenn die Fahrbahn dort zu Ende ist
	 */
	public FahrStreifenLage getLinksVonHier() {
		FahrStreifenLage ergebnis = null;

		if (equals(FahrStreifenLage.HFS)) {
			ergebnis = FahrStreifenLage.UFS1;
		} else if (equals(FahrStreifenLage.UFS1)) {
			ergebnis = FahrStreifenLage.UFS2;
		} else if (equals(FahrStreifenLage.UFS2)) {
			ergebnis = FahrStreifenLage.UFS3;
		} else if (equals(FahrStreifenLage.UFS3)) {
			ergebnis = FahrStreifenLage.UFS4;
		} else if (equals(FahrStreifenLage.UFS4)) {
			ergebnis = FahrStreifenLage.UFS5;
		} else if (equals(FahrStreifenLage.UFS5)) {
			ergebnis = FahrStreifenLage.UFS6;
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Lage des Fahrtreifens rechts vom Fahstreifen mit dieser Lage
	 * (in Fahrtrichtung).
	 *
	 * @return die Lage des Fahrtreifens rechts vom Fahstreifen mit dieser Lage
	 *         oder <code>null</code>, wenn die Fahrbahn dort zu Ende ist
	 */
	public FahrStreifenLage getRechtsVonHier() {
		FahrStreifenLage ergebnis = null;

		if (equals(FahrStreifenLage.UFS1)) {
			ergebnis = FahrStreifenLage.HFS;
		} else if (equals(FahrStreifenLage.UFS2)) {
			ergebnis = FahrStreifenLage.UFS1;
		} else if (equals(FahrStreifenLage.UFS3)) {
			ergebnis = FahrStreifenLage.UFS2;
		} else if (equals(FahrStreifenLage.UFS4)) {
			ergebnis = FahrStreifenLage.UFS3;
		} else if (equals(FahrStreifenLage.UFS5)) {
			ergebnis = FahrStreifenLage.UFS4;
		} else if (equals(FahrStreifenLage.UFS6)) {
			ergebnis = FahrStreifenLage.UFS5;
		}

		return ergebnis;
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code
	 */
	public static FahrStreifenLage getZustand(final int code) {
		return FahrStreifenLage.werteBereich.get(code);
	}
}
