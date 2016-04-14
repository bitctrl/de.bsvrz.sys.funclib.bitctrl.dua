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

import com.bitctrl.Constants;

/**
 * Klasse, die alle Markierungen eines Messwertes speichert.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class MesswertMarkierung implements Cloneable {

	/**
	 * der Wert von <code>*.Status.Erfassung.NichtErfasst</code>.
	 */
	private boolean nichtErfasst;

	/**
	 * der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>.
	 */
	private boolean implausibel;

	/**
	 * der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>.
	 */
	private boolean interpoliert;

	/**
	 * der Wert von <code>*.Status.PlFormal.WertMax</code>.
	 */
	private boolean formalMax;

	/**
	 * der Wert von <code>*.Status.PlFormal.WertMin</code>.
	 */
	private boolean formalMin;

	/**
	 * der Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>.
	 */
	private boolean logischMax;

	/**
	 * der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>.
	 */
	private boolean logischMin;

	/**
	 * zeigt an, ob eine der Setter-Methoden benutzt wurde.
	 */
	private boolean veraendert;

	/**
	 * Erfragt den Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 * .
	 *
	 * @return der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final boolean isInterpoliert() {
		return interpoliert;
	}

	/**
	 * Setzt den Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>.
	 *
	 * @param interpoliert
	 *            der Wert von
	 *            <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final void setInterpoliert(final boolean interpoliert) {
		veraendert = true;
		this.interpoliert = interpoliert;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>.
	 *
	 * @return der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	public final boolean isImplausibel() {
		return implausibel;
	}

	/**
	 * Setzt den Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>.
	 *
	 * @param implausibel
	 *            der Wert von
	 *            <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	public final void setImplausibel(final boolean implausibel) {
		veraendert = true;
		this.implausibel = implausibel;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.Erfassung.NichtErfasst</code>.
	 *
	 * @return der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	public final boolean isNichtErfasst() {
		return nichtErfasst;
	}

	/**
	 * Setzt den Wert von <code>*.Status.Erfassung.NichtErfasst</code>.
	 *
	 * @param nichtErfasst
	 *            der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	public final void setNichtErfasst(final boolean nichtErfasst) {
		veraendert = true;
		this.nichtErfasst = nichtErfasst;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlFormal.WertMax</code>.
	 *
	 * @return den Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	public final boolean isFormalMax() {
		return formalMax;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlFormal.WertMax</code>.
	 *
	 * @param formalMax
	 *            der Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	public final void setFormalMax(final boolean formalMax) {
		veraendert = true;
		this.formalMax = formalMax;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlFormal.WertMin</code>.
	 *
	 * @return den Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	public final boolean isFormalMin() {
		return formalMin;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlFormal.WertMin</code>.
	 *
	 * @param formalMin
	 *            der Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	public final void setFormalMin(final boolean formalMin) {
		veraendert = true;
		this.formalMin = formalMin;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>.
	 *
	 * @return den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	public final boolean isLogischMax() {
		return logischMax;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>.
	 *
	 * @param logischMax
	 *            der Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	public final void setLogischMax(final boolean logischMax) {
		veraendert = true;
		this.logischMax = logischMax;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>.
	 *
	 * @return der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	public final boolean isLogischMin() {
		return logischMin;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>.
	 *
	 * @param logischMin
	 *            der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	public final void setLogischMin(final boolean logischMin) {
		veraendert = true;
		this.logischMin = logischMin;
	}

	/**
	 * Erfragt, ob dieser Wert veraendert wurde.
	 *
	 * @return ob dieser Wert veraendert wurde
	 */
	public final boolean isVeraendert() {
		return veraendert;
	}

	/**
	 * setzt den Änderungszustand.
	 * @param veraendert der Zustand
	 */
	public void setVeraendert(final boolean veraendert) {
		this.veraendert = veraendert;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if (obj instanceof MesswertMarkierung) {
			final MesswertMarkierung that = (MesswertMarkierung) obj;
			gleich = (nichtErfasst == that.nichtErfasst) && (implausibel == that.implausibel)
					&& (interpoliert == that.interpoliert) && (formalMax == that.formalMax)
					&& (formalMin == that.formalMin) && (logischMax == that.logischMax)
					&& (logischMin == that.logischMin);
		}

		return gleich;
	}

	@Override
	public String toString() {
		return (nichtErfasst ? "nErf " : Constants.EMPTY_STRING) + (formalMax ? "fMax " : Constants.EMPTY_STRING)
				+ (formalMin ? "fMin " : Constants.EMPTY_STRING) + (logischMax ? "lMax " : Constants.EMPTY_STRING)
				+ (logischMin ? "lMin " : Constants.EMPTY_STRING) + (implausibel ? "Impl " : Constants.EMPTY_STRING)
				+ (interpoliert ? "Intp " : Constants.EMPTY_STRING);
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Kein hashCode() ermittelbar");
	}

}
