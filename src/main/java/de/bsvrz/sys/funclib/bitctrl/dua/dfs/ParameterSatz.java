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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse enthält alle Parameter, die innerhalb eines Datensatzes
 * <code>ParameterSatz</code> der Attributgruppe
 * <code>atg.datenflussSteuerung</code> vorkommen. Pro SWE wird nur ein
 * Parametersatz vorgehalten. Sollten also innerhalb dieser Attributgruppe
 * mehrere Parametersätze für die gleiche SWE vorkommen, so werden diese
 * (später) gemischt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class ParameterSatz {

	/**
	 * die SWE, deren Publikationsparameter in dieser Klasse stehen.
	 */
	private SWETyp swe;

	/**
	 * alle Publikationszuordnungen dieses Parametersatzes.
	 */
	private final List<PublikationsZuordung> pubZuordnungen = new ArrayList<>();

	/**
	 * Erfragt die SWE, für die Publikationsparameter in dieser Klasse stehen.
	 *
	 * @return die SWE
	 */
	public final SWETyp getSwe() {
		return swe;
	}

	/**
	 * Setzt die SWE, für die Publikationsparameter in dieser Klasse stehen.
	 *
	 * @param swe
	 *            die SWE
	 */
	public final void setSwe(final SWETyp swe) {
		this.swe = swe;
	}

	/**
	 * Erfragt eine Liste mit allen Publikationszuordnungen dieses
	 * Parametersatzes.
	 *
	 * @return alle Publikationszuordnungen dieses Parametersatzes (oder eine
	 *         leere Liste)
	 */
	public final List<PublikationsZuordung> getPubZuordnung() {
		return pubZuordnungen;
	}

	/**
	 * Fügt der Liste aller Publikationszuordnungen eine neue
	 * Publikationszuordnung hinzu. Bevor dies geschieht, werden alle schon
	 * vorhandenen Publikationszuordnungen auf Konsistenz mit der neuen
	 * Publikationszuordnung getestet. Fällt dieser Test negativ aus, so wird
	 * die neue Publikationszuordnung ignoriert und eine den Fehler
	 * dokumentierende Warnung ausgegeben.
	 *
	 * @param pubZuordnung
	 *            neue Publikationszuordnung
	 */
	public final void add(final PublikationsZuordung pubZuordnung) {
		boolean addErlaubt = true;

		for (final PublikationsZuordung altePz : pubZuordnungen) {
			final String fehler = altePz.isKompatibelMit(pubZuordnung);
			if (fehler != null) {
				Debug.getLogger().warning(fehler);
				addErlaubt = false;
				break;
			}
		}

		if (addErlaubt) {
			pubZuordnungen.add(pubZuordnung);
		}
	}

	@Override
	public String toString() {
		String s = "SWE: " + swe + "\n";

		for (final PublikationsZuordung pz : pubZuordnungen) {
			s += pz + "\n";
		}

		return s;
	}

}
