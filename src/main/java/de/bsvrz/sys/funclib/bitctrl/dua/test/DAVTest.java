/*
 * Allgemeine Funktionen f�r das Segment DuA
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.test;

import java.util.Random;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Stellt eine Datenverteiler-Verbindung zur Verf�gung.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class DAVTest {

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected static ClientDavInterface verbindung;

	/**
	 * Randomizer.
	 */
	public static final Random RANDOM = new Random(System.currentTimeMillis());

	/**
	 * Standardkonstruktor.
	 */
	private DAVTest() {
		//
	}

	/**
	 * Erfragt bzw. initialisiert eine Datenverteiler-Verbindung
	 *
	 * @param kommandoZeile
	 *            String-Array mit den Daten, die normalerweise in der
	 *            Kommandozeile stehen
	 * @return die Datenverteiler-Verbindung
	 * @throws Exception
	 *             falls die Verbindung nicht hergestellt werden konnte
	 */
	public static ClientDavInterface getDav(final String[] kommandoZeile) throws Exception {

		if (DAVTest.verbindung == null) {
			DAVTest.verbindung = DAVTest.newDav(kommandoZeile);
		}

		return DAVTest.verbindung;
	}

	/**
	 * Stellt eine neue Datenverteiler-Verbindung her.
	 *
	 * @param kommandoZeile
	 *            String-Array mit den Daten, die normalerweise in der
	 *            Kommandozeile stehen
	 * @return die Datenverteiler-Verbindung
	 * @throws Exception
	 *             falls die Verbindung nicht hergestellt werden konnte
	 */
	public static ClientDavInterface newDav(final String[] kommandoZeile) throws Exception {

		StandardApplicationRunner.run(new StandardApplication() {

			@Override
			public void initialize(final ClientDavInterface connection) throws Exception {
				DAVTest.verbindung = connection;
			}

			@Override
			public void parseArguments(final ArgumentList argumentList) throws Exception {
				argumentList.fetchUnusedArguments();
			}

		}, kommandoZeile);

		return DAVTest.verbindung;
	}

	/**
	 * Wartet bis zu dem �bergebenen Zeitpunkt.
	 *
	 * @param zeitStempel
	 *            ein Zeitstempel in ms
	 */
	public static void warteBis(final long zeitStempel) {
		while (System.currentTimeMillis() <= zeitStempel) {
			try {
				Thread.sleep(5L);
			} catch (final InterruptedException ex) {
				//
			}
		}
	}

	/**
	 * Erfragt einen Array mit zuf�lligen Zahlen von 0 bis <code>anzahl</code>.
	 * Jede Zahl darf nur einmal im Array vorkommen.
	 *
	 * @param anzahl
	 *            die Obergrenze
	 * @return Array mit zuf�lligen Zahlen von 0 bis <code>anzahl</code>
	 */
	public static int[] getZufaelligeZahlen(final int anzahl) {
		int belegt = 0;
		final int[] zahlen = new int[anzahl];
		for (int i = 0; i < anzahl; i++) {
			zahlen[i] = -1;
		}

		while (belegt < anzahl) {
			final int index = DAVTest.RANDOM.nextInt(anzahl);
			if (zahlen[index] == -1) {
				zahlen[index] = belegt++;
			}
		}

		return zahlen;
	}

}
