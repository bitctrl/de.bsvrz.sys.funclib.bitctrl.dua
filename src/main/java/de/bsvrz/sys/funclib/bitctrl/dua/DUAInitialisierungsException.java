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
 * Ausnahme, die geworfen wird, wenn ein Modul innerhalb einer SWE nicht
 * initialisiert werden konnte. Also, wenn z.B. keine Anmeldung zum Empfangen
 * oder Versenden von Daten durchgeführt werden konnte.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class DUAInitialisierungsException extends Exception {

	/**
	 * die Fehlermeldung.
	 */
	private final String meldung;

	/**
	 * Standardkonstruktor Ausnahmen mit Fehlermeldungen.
	 *
	 * @param meldung
	 *            die Fehlermeldung
	 */
	public DUAInitialisierungsException(final String meldung) {
		super();
		this.meldung = meldung;
	}

	/**
	 * Standardkonstruktor für das Weiterreichen von Ausnahmen.
	 *
	 * @param meldung
	 *            die Fehlermeldung
	 * @param t
	 *            die ursprüngliche Ausnahme
	 */
	public DUAInitialisierungsException(final String meldung, final Throwable t) {
		super(t);
		this.meldung = meldung;
	}

	@Override
	public String getMessage() {
		return meldung == null ? super.getMessage() : meldung;
	}
}
