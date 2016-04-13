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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen;

import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;

/**
 * Dieses Interface stellt alle Informationen über die aktuelle
 * Datenflusssteuerung zur Verfügung. Im Wesentlichen stellt es den Zugriff auf
 * ein Objekt des Typs <code>typ.datenflussSteuerung</code> sicher.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public interface IDatenFlussSteuerung {

	/**
	 * Erfragt eine Schnittstelle zu allen Informationen der Datenflusssteuerung
	 * der übergebenen SWE und des übergebenen Modul-Typs.
	 *
	 * @param swe
	 *            die SWE
	 * @param modulTyp
	 *            der Modul-Typ
	 * @return eine Schnittstelle zu allen Informationen der Datenflusssteuerung
	 *         der übergebenen SWE und des übergebenen Modul-Typs. Wenn keine
	 *         Informationen zur SWE/Modul-Typ- Kombination vorhanden sind, wird
	 *         ein leeres Objekt zurückgegeben.
	 */
	IDatenFlussSteuerungFuerModul getDFSFuerModul(final SWETyp swe, final ModulTyp modulTyp);

}
