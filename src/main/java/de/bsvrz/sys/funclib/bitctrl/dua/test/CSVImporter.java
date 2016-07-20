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

package de.bsvrz.sys.funclib.bitctrl.dua.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Allgemeine Klasse zum Lesen aus CSV-Dateien.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class CSVImporter {

	/**
	 * CSV-Datei.
	 */
	private final File csvDatei;

	/**
	 * der Reader für die CSV-Datei.
	 */
	private BufferedReader leser;

	/**
	 * die Zeile, auf der im Moment der Datei-Zeier steht.
	 */
	private int zeilenNummer = -1;

	/**
	 * Standardkonstruktor Nr.1.
	 *
	 * @param csvDatei
	 *            CSV-Datei
	 * @throws Exception
	 *             wenn die Datei nicht geöffnet werden kann
	 */
	public CSVImporter(final File csvDatei) throws Exception {
		this.csvDatei = csvDatei;
		leser = new BufferedReader(new InputStreamReader(new FileInputStream(csvDatei), Charset.defaultCharset()));
	}

	/**
	 * Standardkonstruktor Nr.2.
	 *
	 * @param csvDateiName
	 *            Name der CSV-Datei (mit oder ohne Suffix)
	 * @throws Exception
	 *             wenn die Datei nicht geöffnet werden kann
	 */
	public CSVImporter(final String csvDateiName) throws Exception {
		String name = csvDateiName;
		if (!name.toLowerCase().endsWith(".csv")) {
			name += ".csv";
		}
		csvDatei = new File(name);
		leser = new BufferedReader(new InputStreamReader(new FileInputStream(csvDatei), Charset.defaultCharset()));
	}

	/**
	 * Gibt alle Spalten einer Zeile der Tabelle als String-Array zurück.
	 *
	 * @return ein String-Array mit den Spalten einer Zeile oder
	 *         <code>null</code>, wenn das Dateiende erreicht ist
	 */
	public final String[] getNaechsteZeile() {
		String[] result = null;

		try {
			final String red = leser.readLine();
			zeilenNummer++;

			if (red != null) {
				result = red.split(";");
			}
		} catch (final IOException ex) {
			Debug.getLogger().error("Fehler beim Lesen aus " + this, ex);
		}

		return result;
	}

	/**
	 * Erfragt die Zeilennummer, auf der im Moment der Datei-Zeiger steht.
	 *
	 * @return die Zeilennummer, auf der im Moment der Datei-Zeiger steht
	 *         <code>-1</code> indiziert, dass noch nicht aus der Datei gelesen
	 *         wurde
	 */
	public final int getZeilenNummer() {
		return zeilenNummer;
	}

	@Override
	public String toString() {
		String s = csvDatei.toString();

		try {
			s = csvDatei.getCanonicalPath();
		} catch (final IOException ex) {
			Debug.getLogger().error(Constants.EMPTY_STRING, ex);
		}

		return s;
	}

	/**
	 * Setzt den Dateizeiger wieder auf Anfang.
	 */
	public final void reset() {
		try {
			leser.close();
			leser = new BufferedReader(new InputStreamReader(new FileInputStream(csvDatei), Charset.defaultCharset()));
		} catch (final IOException ex) {
			Debug.getLogger().error(Constants.EMPTY_STRING, ex);
		}
	}

	@Override
	public void finalize() throws Throwable {
		if (leser != null) {
			leser.close();
		}
		super.finalize();
	}

}
