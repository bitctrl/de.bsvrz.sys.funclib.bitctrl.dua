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

package de.bsvrz.sys.funclib.bitctrl.dua.av;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;

/**
 * Repräsentiert die Anmeldung eines <b>finalen</b> Systemobjekts (ein finales
 * Systemobjekt ist entweder ein Konfigurationsobjekt oder ein Dynamisches
 * Objekt) unter einer bestimmten Datenbeschreibung.<br>
 * <b>Achtung:</b>
 * <ul>
 * <li>Diese Klasse ist so entworfen, dass nur im Sinne des Datenverteilers
 * kompatible Objekt-Attributgruppe- Aspekt-Kombinationen akzeptiert werden (via
 * Konstruktor).</li>
 * <li>Weiterhin ist diese Klasse so entworfen, dass beim Einspeisen ihrer
 * Elemente in <code>TreeSet</code>- oder <code>TreeMap</code>-Strukturen keine
 * Datenverteiler-spezifischen Widersprüche innerhalb dieser Strukturen
 * auftreten können. D.h. insbesondere, dass alle Elemente einer solchen
 * Struktur konfliktfrei zum Senden oder Empfangen von Daten angemeldet werden
 * können.<br>
 * Mit konfliktfrei im Sinne des Datenverteilers ist gemeint, dass in einer
 * solchen Struktur keine Objekt- Attributgruppe-Aspekt-Kombinationen doppelt
 * auftreten.</li>
 * </ul>
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DAVObjektAnmeldung implements Comparable<DAVObjektAnmeldung> {

	/**
	 * Das (finale) Systemobjekt.
	 */
	private final SystemObject objekt;

	/**
	 * Die Datenbeschreibung unter der das Systemobjekt angemeldet werden soll
	 * bzw. ist
	 */
	private final DataDescription datenBeschreibung;

	/**
	 * Standardkonstruktor.
	 *
	 * @param objekt
	 *            das (finale) Systemobjekt
	 * @param datenBeschreibung
	 *            die Datenbeschreibung unter der das Systemobjekt angemeldet
	 *            werden soll bzw. ist
	 * @throws IllegalArgumentException
	 *             wenn entweder das Systemobjekt, die Datenbeschreibung, deren
	 *             Attributgruppe oder deren Aspekt <code>null</code> ist, wenn
	 *             die Objekt- Attributgruppen-Aspekt-Kombination an sich
	 *             ungültig bzw. inkompatibel ist, oder wenn das übergebene
	 *             Systemobjekt kein Konfigurationsobjekt oder Dynamisches
	 *             Objekt ist.
	 */
	public DAVObjektAnmeldung(final SystemObject objekt, final DataDescription datenBeschreibung) {
		final String fehler = DUAUtensilien.isKombinationOk(objekt, datenBeschreibung);
		if (fehler != null) {
			throw new IllegalArgumentException(fehler);
		}

		this.objekt = objekt;
		this.datenBeschreibung = datenBeschreibung;
	}

	/**
	 * Macht aus einem <code>ResultData</code>-Objekt ein
	 * <code>DAVObjektAnmeldung</code>-Objekt (über den Aufruf des
	 * Standardkontruktors).
	 *
	 * @param resultat
	 *            ein <code>ResultData</code>-Objekt
	 * @throws IllegalArgumentException
	 *             wenn das <code>ResultData</code>-Objekt <code>null</code>
	 *             sien sollte, oder wenn der Standardkonstruktor eine Exception
	 *             wirft.
	 */
	public DAVObjektAnmeldung(final ResultData resultat) {
		this(resultat.getObject(), resultat.getDataDescription());
	}

	/**
	 * Erfragt die Datenbeschreibung unter der das Systemobjekt angemeldet
	 * werden soll bzw. ist
	 *
	 * @return datenBeschreibung eine Datenbeschreibung
	 */
	public final DataDescription getDatenBeschreibung() {
		return datenBeschreibung;
	}

	/**
	 * Erfragt das Systemobjekt.
	 *
	 * @return objekt ein Systenobjekt
	 */
	public final SystemObject getObjekt() {
		return objekt;
	}

	@Override
	public int compareTo(final DAVObjektAnmeldung that) {
		int result = Long.valueOf(getObjekt().getId()).compareTo(that.getObjekt().getId());

		if (result == 0) {
			result = Long.valueOf(getDatenBeschreibung().getAttributeGroup().getId())
					.compareTo(that.getDatenBeschreibung().getAttributeGroup().getId());
		}
		if (result == 0) {
			result = Long.valueOf(getDatenBeschreibung().getAspect().getId())
					.compareTo(that.getDatenBeschreibung().getAspect().getId());
		}

		return result;
	}

	/**
	 * Diese Methode muss implementiert werden, da nach der Exploration des
	 * Baums über <code>compareTo(..)</code> (bspw. beim Aufruf von
	 * <code>contains()</code>) nochmals mit <code>equals(..)</code> explizit
	 * auf Gleichheit getestet wird.
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean result = false;

		if (obj instanceof DAVObjektAnmeldung) {
			final DAVObjektAnmeldung that = (DAVObjektAnmeldung) obj;
			result = getObjekt().equals(that.getObjekt())
					&& getDatenBeschreibung().getAttributeGroup()
							.equals(that.getDatenBeschreibung().getAttributeGroup())
					&& getDatenBeschreibung().getAspect().equals(that.getDatenBeschreibung().getAspect());
		}

		return result;
	}

	@Override
	public String toString() {
		return objekt + "\n" + datenBeschreibung + "\n";
	}
}
