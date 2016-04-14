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

import de.bsvrz.dav.daf.main.Data;

/**
 * Messwert <b>für ein Attribut</b> mit Plausibilisierungsinformationen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class AbstraktMesswert extends MesswertMarkierung implements Comparable<AbstraktMesswert> {

	/**
	 * Der Attributname dieses Messwertes.
	 */
	private final String attName;

	/**
	 * der Messwert als <code>double</code>.
	 */
	private double wertSkaliert = -4;

	/**
	 * der Messwert als <code>long</code>.
	 */
	private long wertUnskaliert = -4;

	/**
	 * der Guete-Index.
	 */
	private GanzZahl guete = GanzZahl.getGueteIndex();

	/**
	 * das Guete-Verfahren.
	 */
	private int verfahren;

	/**
	 * Standardkonstruktor.
	 *
	 * @param attName
	 *            der Attributname dieses Messwertes
	 * @param datum
	 *            das Datum aus dem der Messwert ausgelesen werden soll
	 */
	public AbstraktMesswert(final String attName, final Data datum) {
		if (attName == null) {
			throw new NullPointerException("Der Attributname ist <<null>>");
		}
		if (datum == null) {
			throw new NullPointerException("Das Datum ist <<null>>");
		}
		this.attName = attName;

		if (!isSkaliert()) {
			wertUnskaliert = datum.getItem(attName).getUnscaledValue("Wert").longValue();
		}

		setNichtErfasst(datum.getItem(attName).getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst")
				.intValue() == DUAKonstanten.JA);
		setFormalMax(datum.getItem(attName).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax")
				.intValue() == DUAKonstanten.JA);
		setFormalMin(datum.getItem(attName).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin")
				.intValue() == DUAKonstanten.JA);

		setLogischMax(datum.getItem(attName).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMaxLogisch")
				.intValue() == DUAKonstanten.JA);
		setLogischMin(datum.getItem(attName).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMinLogisch")
				.intValue() == DUAKonstanten.JA);

		setImplausibel(datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung")
				.getUnscaledValue("Implausibel").intValue() == DUAKonstanten.JA);
		setInterpoliert(datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung")
				.getUnscaledValue("Interpoliert").intValue() == DUAKonstanten.JA);

		guete.setWert(datum.getItem(attName).getItem("Güte").getUnscaledValue("Index").longValue());
		verfahren = datum.getItem(attName).getItem("Güte").getUnscaledValue("Verfahren").intValue();
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param attName
	 *            der Attributname dieses Messwertes
	 */
	public AbstraktMesswert(final String attName) {
		this.attName = attName;
	}

	/**
	 * Erfragt, ob es sich um einen Wert handelt, der skaliert gelesen bzw.
	 * geschrieben werden soll
	 *
	 * @return ob es sich um einen Wert handelt, der skaliert gelesen bzw.
	 *         geschrieben werden soll
	 */
	public abstract boolean isSkaliert();

	/**
	 * Erfragt die Guete dieses Attributwertes.
	 *
	 * @return die Guete dieses Attributwertes
	 */
	public final GanzZahl getGueteIndex() {
		return guete;
	}

	/**
	 * Setzte die Guete dieses Attributwertes.
	 *
	 * @param guete1
	 *            die Guete dieses Attributwertes
	 */
	public final void setGueteIndex(final GanzZahl guete1) {
		guete = guete1;
	}

	/**
	 * Erfragt das Gueteverfahren.
	 *
	 * @return das Gueteverfahren
	 */
	public final int getVerfahren() {
		return verfahren;
	}

	/**
	 * Setzt das Gueteverfahren.
	 *
	 * @param verfahren
	 *            das Gueteverfahren
	 */
	public final void setVerfahren(final int verfahren) {
		this.verfahren = verfahren;
	}

	/**
	 * Setzt den skalierten Attributwert.
	 *
	 * @param wert
	 *            der skalierte Attributwert
	 */
	public final void setWertSkaliert(final double wert) {
		wertSkaliert = wert;
	}

	/**
	 * Erfragt den skalierten Attributwert.
	 *
	 * @return den skalierten Attributwert
	 */
	public final double getWertSkaliert() {
		return wertSkaliert;
	}

	/**
	 * Setzt den unskalierte Attributwert.
	 *
	 * @param wert
	 *            der unskalierte Attributwert
	 */
	public final void setWertUnskaliert(final long wert) {
		wertUnskaliert = wert;
	}

	/**
	 * Erfragt den unskalierten Attributwert.
	 *
	 * @return der unskalierte Attributwert
	 */
	public final long getWertUnskaliert() {
		return wertUnskaliert;
	}

	/**
	 * Erfragt, ob dieser Messwert entweder <code>fehlerhaft</code>,
	 * <code>nicht ermittelbar/fehlerhaft</code> oder <code>implausibel</code>
	 * ist.
	 *
	 * @return ob dieser Messwert entweder <code>fehlerhaft</code>,
	 *         <code>nicht ermittelbar/fehlerhaft</code> oder
	 *         <code>implausibel</code> ist
	 */
	public final boolean isFehlerhaftBzwImplausibel() {
		return (wertUnskaliert == DUAKonstanten.FEHLERHAFT)
				|| (wertUnskaliert == DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT) || isImplausibel();
	}

	@Override
	public int compareTo(final AbstraktMesswert that) {
		return isSkaliert() ? new Double(getWertSkaliert()).compareTo(that.getWertSkaliert())
				: new Long(getWertUnskaliert()).compareTo(that.getWertUnskaliert());
	}

	/**
	 * Kopiert den Inhalt dieses Objektes in das übergebene Datum.
	 *
	 * @param datum
	 *            ein veränderbares Datum
	 */
	public final void kopiereInhaltNach(final Data datum) {
		if (isSkaliert()) {
			datum.getItem(attName).getScaledValue("Wert").set(wertSkaliert);
		} else {
			if (DUAUtensilien.isWertInWerteBereich(datum.getItem(attName).getItem("Wert"), wertUnskaliert)) {
				datum.getItem(attName).getUnscaledValue("Wert").set(wertUnskaliert);
			} else {
				datum.getItem(attName).getUnscaledValue("Wert").set(DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT);
			}
		}

		datum.getItem(attName).getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst")
		.set(isNichtErfasst() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax")
		.set(isFormalMax() ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin")
		.set(isFormalMin() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMaxLogisch")
		.set(isLogischMax() ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMinLogisch")
		.set(isLogischMin() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel")
		.set(isImplausibel() ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Interpoliert")
		.set(isInterpoliert() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Güte").getUnscaledValue("Index").set(guete.getWert());
		datum.getItem(attName).getItem("Güte").getUnscaledValue("Verfahren").set(verfahren);
	}

	/**
	 * Kopiert den Inhalt dieses Objektes in das übergebene Datum.
	 *
	 * @param datum
	 *            ein veränderbares Datum
	 */
	public final void kopiereInhaltNachModifiziereIndex(final Data datum) {
		if (isSkaliert()) {
			datum.getItem(attName).getScaledValue("Wert").set(wertSkaliert);
		} else {
			if (DUAUtensilien.isWertInWerteBereich(datum.getItem(attName).getItem("Wert"), wertUnskaliert)) {
				datum.getItem(attName).getUnscaledValue("Wert").set(wertUnskaliert);
			} else {
				datum.getItem(attName).getUnscaledValue("Wert").set(DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT);
			}
		}

		datum.getItem(attName).getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst")
		.set(isNichtErfasst() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax")
		.set(isFormalMax() ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin")
		.set(isFormalMin() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMaxLogisch")
		.set(isLogischMax() ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMinLogisch")
		.set(isLogischMin() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel")
		.set(isImplausibel() ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Interpoliert")
		.set(isInterpoliert() ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		if (datum.getItem(attName).getUnscaledValue("Wert").longValue() < 0) {
			datum.getItem(attName).getItem("Güte").getUnscaledValue("Index").set(0);
		} else {
			datum.getItem(attName).getItem("Güte").getUnscaledValue("Index").set(guete.getWert());
		}
		datum.getItem(attName).getItem("Güte").getUnscaledValue("Verfahren").set(verfahren);
	}

	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if (obj instanceof AbstraktMesswert) {
			final AbstraktMesswert that = (AbstraktMesswert) obj;

			gleich = super.equals(obj) && (getWertUnskaliert() == that.getWertUnskaliert()) && guete.equals(that.guete);
		}

		return gleich;
	}

	@Override
	public String toString() {
		return (isSkaliert() ? getWertSkaliert() : getWertUnskaliert()) + " " + super.toString() + " "
				+ guete.getSkaliertenWert() + " (" + verfahren + ")";
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Kein hashCode() ermittelbar");
	}

	/**
	 * Erfragt den Namen dieses Messwertes.
	 *
	 * @return der Name dieses Messwertes
	 */
	public final String getName() {
		return attName;
	}

}
