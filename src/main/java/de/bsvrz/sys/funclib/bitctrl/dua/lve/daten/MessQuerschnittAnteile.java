/*
 * Allgemeine Funktionen für das Segment DuA
 * Copyright 2016 by Kappich Systemberatung Aachen
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
 */

package de.bsvrz.sys.funclib.bitctrl.dua.lve.daten;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Interface für allgemeine Bestandteile eines virtuellen Messquerschnitts
 * (Verfahren VLage und Allgemein).
 *
 * @author Kappich Systemberatung
 */
public interface MessQuerschnittAnteile {
	/**
	 * Erfragt Liste der Messquerschnitte mit Angabe der Berechnungsvorschrift,
	 * wie aus diesen Messquerschnittswerten die Werte des virtuellen
	 * Messquerschnitts ermittelt werden sollen.
	 *
	 * @return (ggf. leere) Liste der Messquerschnitte mit Angabe der
	 *         Berechnungsvorschrift.
	 */
	AtgMessQuerschnittVirtuellVLage.AtlMessQuerSchnittBestandTeil[] getMessQuerSchnittBestandTeile();

	/**
	 * Erfragt den Messquerschnitt von dem die Geschwindigkeit uebernommen
	 * werden soll. Ist dieser nicht explizit versorgt wird null zurückgegeben
	 *
	 * @return der Messquerschnitt von dem die Geschwindigkeit uebernommen
	 *         werden soll.
	 */
	SystemObject getMessQuerschnittGeschwindigkeit();
}
