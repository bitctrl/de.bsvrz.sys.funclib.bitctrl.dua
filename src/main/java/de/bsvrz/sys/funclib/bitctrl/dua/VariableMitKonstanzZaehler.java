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

/**
 * Beinhaltet eine Variable und die Information wie lange diese Variable schon
 * konstant ist (d.h. wie oft sie mittels der Methode
 * <code>aktualisiere(..)</code> aktualisiert wurde und sich dabei nicht
 * geändert hat)
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <T>
 *            Art der Variable
 */
public class VariableMitKonstanzZaehler<T> {

	/**
	 * Der Name der Variable.
	 */
	private final String name;

	/**
	 * der aktuelle Wert.
	 */
	private T wert;

	/**
	 * so lange hat die Variable schon den selben Wert.
	 */
	private long wertIstKonstantSeit;

	/**
	 * Standardkonstruktor.
	 *
	 * @param name
	 *            der Name der Variable
	 */
	public VariableMitKonstanzZaehler(final String name) {
		this.name = name;
	}

	/**
	 * Aktualisiert dieses Objekt mit einem neuen Wert für die Variable.
	 *
	 * @param neuerWert
	 *            ein aktueller Wert
	 */
	public final void aktualisiere(final T neuerWert) {
		if ((this.wert == null) || !this.wert.equals(neuerWert)) {
			this.wertIstKonstantSeit = 1;
		} else {
			this.wertIstKonstantSeit++;
		}

		this.wert = neuerWert;
	}

	/**
	 * Aktualisiert dieses Objekt mit einem neuen Wert für die Variable und
	 * übergibt ein Inkrement um das der interne Konstanzzähler der Variable
	 * erhöht werden soll. (Etwa, wenn dieser Zähler zählen soll, wie lange eine
	 * die Variable konstant ist, kann hier eine Zeit übergeben werden).
	 *
	 * @param neuerWert
	 *            ein aktueller Wert
	 * @param inkrement
	 *            ein Inkrement
	 */
	public final void aktualisiere(final T neuerWert, final long inkrement) {
		if ((this.wert == null) || !this.wert.equals(neuerWert)) {
			this.wertIstKonstantSeit = inkrement;
		} else {
			this.wertIstKonstantSeit += inkrement;
		}

		this.wert = neuerWert;
	}

	/**
	 * Erfragt den aktuellen Werte dieser Variable.
	 *
	 * @return wert der aktuelle Wert
	 */
	public final T getWert() {
		return this.wert;
	}

	/**
	 * Erfragt, seit wann diese Variable konstant ist.
	 *
	 * @return 0 - wenn die Variable noch nie aktualisiert wurde und sonst die
	 *         Anzahl der Inkremente seit der aktuelle Wert anliegt
	 */
	public final long getWertIstKonstantSeit() {
		return this.wertIstKonstantSeit;
	}

	/**
	 * Erfragt den Namen dieser Variable.
	 *
	 * @return der Name dieser Variable
	 */
	public final String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		String s = this.name + " ist seit " + this.wertIstKonstantSeit + " Intervallen konstant " + this.wert;

		if (this.wert == null) {
			s = this.name + " wurde noch nicht beschrieben";
		}

		return s;
	}

}
