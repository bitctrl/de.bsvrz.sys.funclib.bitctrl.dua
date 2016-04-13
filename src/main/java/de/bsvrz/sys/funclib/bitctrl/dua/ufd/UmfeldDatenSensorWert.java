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

package de.bsvrz.sys.funclib.bitctrl.dua.ufd;

import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Schnittstelle zu einem Roh-Sensorwert (dem eigentlichen Wert) eines
 * Umfelddatensensors <b>ohne</b> Plausibilisierungs-Informationen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class UmfeldDatenSensorWert implements Comparable<UmfeldDatenSensorWert> {

	/**
	 * Dieses Flag bestimmt, ob Werte, die laut Anforderungen als fehlerhaft zu
	 * setzen sind, auch tatsächlich so gesetzt werden. Wenn man dieses Flag auf
	 * true setzt, so wird ein als implausibel gekennzeichneter Wert trotzdem
	 * publiziert.
	 */
	private static boolean fehlerhafteWertePublizieren;

	/**
	 * der Wert an sich.
	 */
	private long wert;

	/**
	 * Daten nicht ermittelbar, da bereits Basiswerte fehlerhaft. Wird gesetzt,
	 * wenn Daten, die zur Berechnung dieses Werts notwendig sind, bereits als
	 * fehlerhaft gekennzeichnet sind, oder wenn die Berechnung aus anderen
	 * Gründen (z.B. Nenner = 0 in der Berechnungsformel) nicht möglich war.
	 */
	private boolean fehlerhaftBzwNichtErmittelbar;

	/**
	 * Daten sind fehlerhaft. Wird gesetzt, wenn die Daten als fehlerhaft
	 * erkannt wurden
	 */
	private boolean fehlerhaft;

	/**
	 * Daten sind nicht ermittelbar (ist KEIN Fehler). Wird gesetzt, wenn der
	 * entsprechende Wert nicht ermittelbar ist und kein Interpolation sinnvoll
	 * möglich ist (z.B. ist die Geschwindigkeit nicht ermittelbar, wenn kein
	 * Fahrzeug erfasst wurde).
	 **/
	private boolean nichtErmittelbar;

	/**
	 * die Datenart des Wertes.
	 */
	private final UmfeldDatenArt datenArt;

	/**
	 * wird von diesem Objekt auf <code>true</code> gesetzt, wenn eine der
	 * Setter-Methoden aufgerufen wurde.
	 */
	private boolean veraendert;

	/**
	 * Standardkonstruktor.
	 *
	 * @param datenArt
	 *            die Datenart des Wertes
	 */
	public UmfeldDatenSensorWert(final UmfeldDatenArt datenArt) {
		this.datenArt = datenArt;
	}

	/**
	 * Erfragt die Skalierung dieses Wertes.
	 *
	 * @return die Skalierung dieses Wertes
	 */
	private double getWertSkalierung() {
		return datenArt.getSkalierung();
	}

	/**
	 * Erfragt den Offset für den Wertestatus dieses UF-Datums in Bezug auf die
	 * normalen Werte:<br>
	 * . - <code>nicht ermittelbar = -1</code><br>
	 * - <code>fehlerhaft = -2</code>, oder<br>
	 * - <code>nicht ermittelbar/fehlerhaft = -3</code><br>
	 *
	 * @return der Offset
	 */
	private long getWertStatusOffset() {
		long offset = 0;

		if (datenArt.equals(UmfeldDatenArt.fbf) || datenArt.equals(UmfeldDatenArt.hk)
				|| datenArt.equals(UmfeldDatenArt.ns) || datenArt.equals(UmfeldDatenArt.ni)
				|| datenArt.equals(UmfeldDatenArt.nm) || datenArt.equals(UmfeldDatenArt.rlf)
				|| datenArt.equals(UmfeldDatenArt.sh) || datenArt.equals(UmfeldDatenArt.sw)
				|| datenArt.equals(UmfeldDatenArt.wfd) || datenArt.equals(UmfeldDatenArt.wr)
				|| datenArt.equals(UmfeldDatenArt.fbz) || datenArt.equals(UmfeldDatenArt.ld)
				|| datenArt.equals(UmfeldDatenArt.rs) || datenArt.equals(UmfeldDatenArt.wgm)
				|| datenArt.equals(UmfeldDatenArt.fbg) || datenArt.equals(UmfeldDatenArt.tsq)
				|| datenArt.equals(UmfeldDatenArt.zg) || datenArt.equals(UmfeldDatenArt.wgs)) {
			offset = 0;
		} else if (datenArt.equals(UmfeldDatenArt.tt1) || datenArt.equals(UmfeldDatenArt.tt2)
				|| datenArt.equals(UmfeldDatenArt.tt3) || datenArt.equals(UmfeldDatenArt.tpt)
				|| datenArt.equals(UmfeldDatenArt.lt) || datenArt.equals(UmfeldDatenArt.gt)
				|| datenArt.equals(UmfeldDatenArt.fbt)) {
			offset = -1000;
		} else {
			throw new RuntimeException("Das Umfelddatum " + datenArt + " kann nicht identifiziert werden");
		}

		return offset;
	}

	/**
	 * Zeigt an, ob nach dem letzten Aufruf von <code>setVeraendert(true)</code>
	 * eine Set-Methode aufgerufen wurde.
	 *
	 * @return ob nach dem letzten Aufruf von <code>setVeraendert(true)</code>
	 *         eine Set-Methode aufgerufen wurde
	 */
	public final boolean isVeraendert() {
		return veraendert;
	}

	/**
	 * Setzt den Wert <code>veraendert</code>.
	 *
	 * @param veraendert
	 *            der Wert <code>veraendert</code>
	 */
	public final void setVeraendert(final boolean veraendert) {
		this.veraendert = veraendert;
	}

	/**
	 * Erfragt den Wert.
	 *
	 * @return wert der Wert
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
	public final void setWert(final long wert) {
		veraendert = true;
		this.wert = wert;
		fehlerhaft = this.wert == (getWertStatusOffset() + DUAKonstanten.FEHLERHAFT);
		fehlerhaftBzwNichtErmittelbar = this.wert == (getWertStatusOffset()
				+ DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT);
		nichtErmittelbar = this.wert == (getWertStatusOffset() + DUAKonstanten.NICHT_ERMITTELBAR);
	}

	/**
	 * Setzt den Wert.
	 *
	 * @param wert1
	 *            festzulegender Wert
	 */
	public final void setSkaliertenWert(final double wert1) {
		final double skalierung = getWertSkalierung();
		setWert(Math.round(wert1 / skalierung));
	}

	/**
	 * Erfragt den Wert.
	 *
	 * @return wert der Wert
	 */
	public final double getSkaliertenWert() {
		return getWert() * getWertSkalierung();
	}

	/**
	 * Setzt das Flag <code>fehlerhaft</code> an.
	 */
	public final void setFehlerhaftAn() {
		if (UmfeldDatenSensorWert.fehlerhafteWertePublizieren) {
			fehlerhaft = true;
		} else {
			setWert(getWertStatusOffset() + DUAKonstanten.FEHLERHAFT);
		}
	}

	/**
	 * Erfragt, ob der Wert fehlerhaft ist.
	 *
	 * @return ob der Wert fehlerhaft ist
	 */
	public final boolean isFehlerhaft() {
		return fehlerhaft;
	}

	/**
	 * Setzt das Flag <code>nicht ermittelbar/fehlerhaft</code> an.
	 */
	public final void setFehlerhaftBzwNichtErmittelbarAn() {
		setWert(getWertStatusOffset() + DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT);
	}

	/**
	 * Erfragt, ob der Wert als nicht ermittelbar gekennzeichnet ist, da bereits
	 * Basiswerte fehlerhaft sind.
	 *
	 * @return ob der Wert als nicht ermittelbar gekennzeichnet ist, da bereits
	 *         Basiswerte fehlerhaft sind
	 */
	public final boolean isFehlerhaftBzwNichtErmittelbar() {
		return fehlerhaftBzwNichtErmittelbar;
	}

	/**
	 * Setzt das Flag <code>nicht ermittelbar</code> an.
	 */
	public final void setNichtErmittelbarAn() {
		setWert(getWertStatusOffset() + DUAKonstanten.NICHT_ERMITTELBAR);
	}

	/**
	 * Erfragt, ob der Wert nicht ermittelbar ist (ist KEIN Fehler).
	 *
	 * @return ob der Wert nicht ermittelbar ist (ist KEIN Fehler).
	 */
	public final boolean isNichtErmittelbar() {
		return nichtErmittelbar;
	}

	/**
	 * Erfragt, ob dieser Wert wirkliche Daten enthält (im Gegensatz zu blos
	 * Statuswerten. )
	 *
	 * @return ob dieser Wert wirkliche Daten enthält (im Gegensatz zu blos
	 *         Statuswerten)
	 */
	public final boolean isOk() {
		return !(nichtErmittelbar || fehlerhaft || fehlerhaftBzwNichtErmittelbar);
	}

	@Override
	public String toString() {
		String wertStr = new Long(wert).toString();

		if (isFehlerhaft()) {
			wertStr = "fehlerhaft";
		} else if (isFehlerhaftBzwNichtErmittelbar()) {
			wertStr = "nicht ermittelbar/fehlerhaft";
		} else if (isNichtErmittelbar()) {
			wertStr = "nicht ermittelbar";
		}

		return wertStr;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if ((obj != null) && (obj instanceof UmfeldDatenSensorWert)) {
			final UmfeldDatenSensorWert that = (UmfeldDatenSensorWert) obj;
			gleich = getWert() == that.getWert();
		}

		return gleich;
	}

	@Override
	public int compareTo(final UmfeldDatenSensorWert that) {
		if (that == null) {
			throw new NullPointerException("Vergleichswert ist <<null>>");
		}
		return new Long(getWert()).compareTo(that.getWert());
	}

	/**
	 * Dieses Flag bestimmt, ob Werte, die laut Anforderungen als fehlerhaft zu
	 * setzen sind, auch tatsächlich so gesetzt werden. Wenn man dieses Flag auf
	 * true setzt, so wird ein als implausibel gekennzeichneter Wert trotzdem
	 * publiziert.
	 *
	 * @param fehlerhafteWertePublizieren
	 *            fehlerhafte Werte publizieren?
	 */
	public static void setFehlerhafteWertePublizieren(final boolean fehlerhafteWertePublizieren) {
		UmfeldDatenSensorWert.fehlerhafteWertePublizieren = fehlerhafteWertePublizieren;
	}

}
