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

import java.nio.channels.UnsupportedAddressTypeException;

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Korrespondiert mit den Eigenschaften einer ggf. skalierbaren DAV-Ganzzahl
 * (mit Zustaenden)
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class GanzZahl implements Comparable<GanzZahl> {

	/**
	 * der Wert an sich.
	 */
	private long wert;

	/**
	 * der Skalierungsfaktor.
	 */
	private double skalierungsFaktor = 1.0;

	/**
	 * Menge der Zustaende dieser Ganzzahl.
	 */
	private AbstractDavZustand[] zustaende;

	/**
	 * der aktuelle Zustand.
	 */
	private AbstractDavZustand aktuellerZustand;

	/***************************************************************************
	 * * statische Methoden zum Anlegen von Variablen * *
	 **************************************************************************/

	/**
	 * Erfragt eine Instanz einer normalen Messwertzahl (unskaliert und mit den
	 * drei Zuständen <code>fehlerhaft</code>, <code>nicht ermittelbar</code>
	 * und <code>nicht ermittelbar/fehlerhaft</code>).
	 *
	 * @return eine Instanz einer normalen Messwertzahl
	 */
	public static final GanzZahl getMWZahl() {
		return new GanzZahl(new AbstractDavZustand[] { MesswertZustand.FEHLERHAFT, MesswertZustand.NICHT_ERMITTELBAR,
				MesswertZustand.FEHLERHAFT_BZW_NICHT_ERMITTELBAR });
	}

	/**
	 * Erfragt eine Instanz eines Gueteindizes (skaliert mit 0,0001 und mit den
	 * drei Zustaenden <code>fehlerhaft</code>, <code>nicht ermittelbar</code>
	 * und <code>nicht ermittelbar/fehlerhaft</code>)<br>
	 * <b>Achtung:</b> Dieser Wert ist standardmaessig mit 1.0 initialisiert.
	 *
	 * @return eine Instanz eines Gueteindizes
	 */
	public static final GanzZahl getGueteIndex() {
		final GanzZahl gueteIndex = new GanzZahl(0.0001, new AbstractDavZustand[] { MesswertZustand.FEHLERHAFT,
				MesswertZustand.NICHT_ERMITTELBAR, MesswertZustand.FEHLERHAFT_BZW_NICHT_ERMITTELBAR });
		gueteIndex.setWert(10000);
		return gueteIndex;
	}

	/**
	 * Standardkonstruktor für Skalierung 1.0 und keine Zustaende.
	 */
	public GanzZahl() {
		//
	}

	/**
	 * Standardkonstruktor mit Skalierungsfaktor.
	 *
	 * @param skalierungsFaktor
	 *            der Skalierungsfaktor
	 */
	public GanzZahl(final double skalierungsFaktor) {
		this.skalierungsFaktor = skalierungsFaktor;
	}

	/**
	 * Standardkonstruktor mit Zustandsmenge.
	 *
	 * @param zustaende
	 *            Menge von Zustaenden
	 */
	public GanzZahl(final AbstractDavZustand[] zustaende) {
		this.zustaende = zustaende;
		setWert(0);
	}

	/**
	 * Standardkonstruktor mit Skalierungsfaktor und Zustandsmenge.
	 *
	 * @param skalierungsFaktor
	 *            der Skalierungsfaktor
	 * @param zustaende
	 *            Menge von Zustaenden
	 */
	public GanzZahl(final double skalierungsFaktor, final AbstractDavZustand[] zustaende) {
		this.skalierungsFaktor = skalierungsFaktor;
		this.zustaende = zustaende;
		setWert(0);
	}

	/**
	 * Kopierkonstruktor.
	 *
	 * @param vorlage
	 *            das zu kopierende <code>GanzZahl</code>-Objekt
	 */
	public GanzZahl(final GanzZahl vorlage) {
		wert = vorlage.wert;
		skalierungsFaktor = vorlage.skalierungsFaktor;
		zustaende = new AbstractDavZustand[vorlage.zustaende.length];
		for (int i = 0; i < vorlage.zustaende.length; i++) {
			zustaende[i] = vorlage.zustaende[i];
		}
		aktuellerZustand = vorlage.aktuellerZustand;
	}

	/**
	 * Erfragt den Wert.
	 *
	 * @return der Wert
	 */
	public final long getWert() {
		return wert;
	}

	/**
	 * Setzt den Wert.
	 *
	 * @param wert
	 *            festzulegender Wert
	 */
	public void setWert(final long wert) {
		this.wert = wert;

		if (zustaende != null) {
			aktuellerZustand = null;
			for (final AbstractDavZustand zustand : zustaende) {
				if (zustand.getCode() == this.wert) {
					aktuellerZustand = zustand;
					break;
				}
			}
		}
	}

	/**
	 * Setzt den (skalierten) Wert.
	 *
	 * @param wert1
	 *            festzulegender (skalierter) Wert
	 */
	public final void setSkaliertenWert(final double wert1) {
		final double skalierung = skalierungsFaktor;
		setWert(Math.round(wert1 / skalierung));
	}

	/**
	 * Erfragt den (skalierten) Wert.
	 *
	 * @return der (skalierte) Wert
	 */
	public final double getSkaliertenWert() {
		return getWert() * skalierungsFaktor;
	}

	/**
	 * Setzt den aktuellen Zustand dieses Wertes.
	 *
	 * @param zustand
	 *            der aktuelle Zustand dieses Wertes
	 */
	public final void setZustand(final AbstractDavZustand zustand) {
		assert zustand != null;
		wert = zustand.getCode();
		aktuellerZustand = zustand;
	}

	/**
	 * Erfragt den aktuellen Zustand dieses Wertes.
	 *
	 * @return der aktuelle Zustand dieses Wertes
	 */
	public final AbstractDavZustand getZustand() {
		return aktuellerZustand;
	}

	/**
	 * Erfragt, ob dieser Wert zur Zeit einen Zustand angenommen hat.
	 *
	 * @return ob dieser Wert zur Zeit einen Zustand angenommen hat
	 */
	public final boolean isZustand() {
		return aktuellerZustand != null;
	}

	@Override
	public String toString() {
		String s = Constants.EMPTY_STRING;

		s += "Wert (unskaliert): " + getWert();
		s += "\nWert (skaliert): " + getSkaliertenWert() + ", (F: " + skalierungsFaktor + ")";
		s += "\nZustaende: ";
		if ((zustaende == null) || (zustaende.length == 0)) {
			s += "keine";
		} else {
			for (final AbstractDavZustand zustand : zustaende) {
				s += "\n" + zustand.toString() + " (" + zustand.getCode() + ")";
			}

			s += "\nAktueller Zustand: ";
			if (isZustand()) {
				s += getZustand().toString() + " (" + getZustand().getCode() + ")";
			} else {
				s += "keiner";
			}
		}

		return s;
	}

	/* FIXME equals und hashCode verletzt den API-Kontrakt und sollte
	 * hier entfernt bzw. überdacht werden. */
	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if ((obj != null) && (obj instanceof GanzZahl)) {
			final GanzZahl that = (GanzZahl) obj;
			gleich = getWert() == that.getWert();
		}

		return gleich;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int compareTo(final GanzZahl that) {
		if (that == null) {
			throw new NullPointerException("Vergleichswert ist <<null>>");
		}
		return new Long(getWert()).compareTo(that.getWert());
	}

}
