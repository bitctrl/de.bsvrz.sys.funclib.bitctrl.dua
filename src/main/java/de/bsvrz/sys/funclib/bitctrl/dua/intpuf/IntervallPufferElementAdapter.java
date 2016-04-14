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

package de.bsvrz.sys.funclib.bitctrl.dua.intpuf;

/**
 * Adapter fuer <code>IIntervallPufferElement</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <T>
 *            ein Intervallpufferelement
 */
public class IntervallPufferElementAdapter<T extends IIntervallDatum<T>> implements IIntervallPufferElement<T> {

	/**
	 * der Inhalt.
	 */
	private  T inhalt;

	/**
	 * Intervallende.
	 */
	private long intervallEnde = -1;

	/**
	 * Intervallanfang.
	 */
	private long intervallStart = -1;

	/**
	 * Standardkonstruktor.
	 *
	 * @param intervallStart
	 *            Intervallanfang
	 * @param intervallEnde
	 *            Intervallende
	 */
	protected IntervallPufferElementAdapter(final long intervallStart, final long intervallEnde) {
		this.intervallStart = intervallStart;
		this.intervallEnde = intervallEnde;
	}

	@Override
	public T getInhalt() {
		return this.inhalt;
	}

	@Override
	public long getIntervallEnde() {
		return this.intervallEnde;
	}

	@Override
	public long getIntervallStart() {
		return this.intervallStart;
	}

	@Override
	public String toString() {
		return "[" + this.intervallStart + ", " + this.intervallEnde + "]: "
				+ (this.inhalt == null ? "leer" : this.inhalt);
	}

	protected void setInhalt(final T inhalt) {
		this.inhalt = inhalt;
	}

	protected void setIntervallStart(final long intervallStart) {
		this.intervallStart = intervallStart;
	}

	protected void setIntervallEnde(final long intervallEnde) {
		this.intervallEnde = intervallEnde;
	}

}
