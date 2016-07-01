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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IObjektWeckerListener;
import de.bsvrz.sys.funclib.bitctrl.dua.test.DAVTest;

/**
 * Testet die Klasse <code>ObjektWecker</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class TestObjektWecker {

	/**
	 * der getestete Wecker.
	 */
	private final ObjektWecker wecker = new ObjektWecker();

	/**
	 * Menge der geweckten Objekte mit der Anzahl, wie oft sie geweckt wurden.
	 */
	private final HashMap<TestWeckObjekt, Integer> geweckteObjekte = new HashMap<>();

	/**
	 * Menge der zu weckenden Objekte mit der Anzahl, wie oft sie geweckt
	 * werden. sollen
	 */
	private final HashMap<TestWeckObjekt, Integer> zuWeckendeObjekte = new HashMap<>();

	/** führt den Test1 aus.
	 * @throws InterruptedException */
	@Test
	public void test1() throws InterruptedException {

		long jetzt = System.currentTimeMillis();
		long jetztPlus2Sek = jetzt + (TimeUnit.SECONDS.toMillis(2));
		long jetztPlus3Sek = jetzt + (TimeUnit.SECONDS.toMillis(3));
		long jetztPlus5Sek = jetzt + (TimeUnit.SECONDS.toMillis(5));
		long jetztPlus7Sek = jetzt + (TimeUnit.SECONDS.toMillis(7));

		final TestWeckObjekt t1 = new TestWeckObjekt("1", jetztPlus2Sek);
		final TestWeckObjekt t2 = new TestWeckObjekt("2", jetztPlus2Sek);
		final TestWeckObjekt t3 = new TestWeckObjekt("3", jetztPlus2Sek);

		TimeUnit.MILLISECONDS.sleep(500L);

		final TestWeckObjekt t4 = new TestWeckObjekt("4", jetztPlus3Sek);
		final TestWeckObjekt t5 = new TestWeckObjekt("5", jetztPlus3Sek);
		final TestWeckObjekt t6 = new TestWeckObjekt("6", jetztPlus3Sek);

		TimeUnit.MILLISECONDS.sleep(2500L);

		final TestWeckObjekt t7 = new TestWeckObjekt("7", jetztPlus7Sek);
		final TestWeckObjekt t8 = new TestWeckObjekt("8", jetztPlus5Sek);
		t8.setNeuenWeckZeitPunkt(jetztPlus7Sek);
		t8.setNeuenWeckZeitPunkt(jetztPlus5Sek);
		t8.setNeuenWeckZeitPunkt(jetztPlus7Sek);

		t7.setNeuenWeckZeitPunkt(jetztPlus5Sek);
		t7.setWeckerAus();

		t1.setNeuenWeckZeitPunkt(jetztPlus5Sek);
		t2.setNeuenWeckZeitPunkt(jetztPlus7Sek);

		DAVTest.warteBis(jetztPlus7Sek + TimeUnit.SECONDS.toMillis(1));
		/**
		 * jetzt alles wieder leer
		 */

		/**
		 * jetzt müssten alle Objekten so oft geweckt worden sein, wie
		 * eingeplant.
		 */
		Assert.assertEquals(zuWeckendeObjekte.keySet().size(), geweckteObjekte.keySet().size());
		for (final TestWeckObjekt objekt : zuWeckendeObjekte.keySet()) {
			Assert.assertEquals("Anzahl der Weckzeiten ungleich: ", zuWeckendeObjekte.get(objekt),
					geweckteObjekte.get(objekt));
		}

		for (int i = 0; i < 10; i++) {
			jetzt = System.currentTimeMillis();

			jetztPlus2Sek = jetzt + (TimeUnit.SECONDS.toMillis(2));
			jetztPlus3Sek = jetzt + (TimeUnit.SECONDS.toMillis(3));
			jetztPlus5Sek = jetzt + (TimeUnit.SECONDS.toMillis(5));
			jetztPlus7Sek = jetzt + (TimeUnit.SECONDS.toMillis(7));

			t1.setNeuenWeckZeitPunkt(jetztPlus7Sek);
			t2.setNeuenWeckZeitPunkt(jetztPlus2Sek);
			t1.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			t1.setNeuenWeckZeitPunkt(jetztPlus3Sek);
			t1.setWeckerAus();

			TimeUnit.MILLISECONDS.sleep(2900);

			t3.setNeuenWeckZeitPunkt(jetztPlus3Sek);

			t4.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			t5.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			t6.setNeuenWeckZeitPunkt(jetztPlus5Sek);

			t4.setNeuenWeckZeitPunkt(jetztPlus7Sek);
			t4.setWeckerAus();

			t5.setNeuenWeckZeitPunkt(jetztPlus7Sek);

			DAVTest.warteBis(jetztPlus7Sek + TimeUnit.SECONDS.toMillis(1));
			/**
			 * jetzt müssten alle Objekten so oft geweckt worden sein, wie
			 * eingeplant
			 */
			Assert.assertEquals(zuWeckendeObjekte.keySet().size(), geweckteObjekte.keySet().size());
			for (final TestWeckObjekt objekt : zuWeckendeObjekte.keySet()) {
				Assert.assertEquals("Anzahl der Weckzeiten ungleich: ", zuWeckendeObjekte.get(objekt),
						geweckteObjekte.get(objekt));
			}
		}
	}

	/**
	 * Instanzen dieser Klasse werden geweckt.
	 */
	private class TestWeckObjekt implements IObjektWeckerListener {

		/**
		 * Zeit-Toleranz beim Wecken in ms.
		 */
		private static final long TOLERANZ = 200;

		/**
		 * der eingeplante Weckzeitpunkt.
		 */
		private long weckZeitPunkt = -1;

		/**
		 * der Name des Objektes.
		 */
		private final String name;

		/**
		 * Standardkonstruktor.
		 *
		 * @param name
		 *            der Name des Objektes
		 * @param weckZeitPunkt
		 *            der eingeplante Weckzeitpunkt
		 */
		TestWeckObjekt(final String name, final long weckZeitPunkt) {
			this.weckZeitPunkt = weckZeitPunkt;
			this.name = name;

			Assert.assertFalse(wecker.isWeckerGestelltFuer(this));

			wecker.setWecker(this, weckZeitPunkt);

			System.out.println("Eingeplant: " + this);
			if (zuWeckendeObjekte.get(this) == null) {
				zuWeckendeObjekte.put(this, 1);
			} else {
				zuWeckendeObjekte.put(this, zuWeckendeObjekte.get(this) + 1);
			}
		}

		/**
		 * Setzt, dass das Objekt nicht mehr geweckt werden soll.
		 */
		public void setWeckerAus() {
			weckZeitPunkt = -1;
			Assert.assertTrue(wecker.isWeckerGestelltFuer(this));

			wecker.setWecker(this, ObjektWecker.AUS);
			Assert.assertFalse(wecker.isWeckerGestelltFuer(this));

			System.out.println("Ausgeplant: " + this);
			zuWeckendeObjekte.put(this, zuWeckendeObjekte.get(this) - 1);
			if (zuWeckendeObjekte.get(this) == 0) {
				zuWeckendeObjekte.remove(this);
			}
		}

		/**
		 * Setzt einen neuen Weckzeitpunkt.
		 *
		 * @param neuerWeckZeitpunkt
		 *            ein neuer Weckzeitpunkt
		 */
		public void setNeuenWeckZeitPunkt(final long neuerWeckZeitpunkt) {

			/**
			 * elter Weckzeitpunkt abgelaufen?
			 */
			if (weckZeitPunkt < System.currentTimeMillis()) {
				if (zuWeckendeObjekte.get(this) == null) {
					zuWeckendeObjekte.put(this, 1);
				} else {
					zuWeckendeObjekte.put(this, zuWeckendeObjekte.get(this) + 1);
				}
			}

			weckZeitPunkt = neuerWeckZeitpunkt;

			wecker.setWecker(this, weckZeitPunkt);

			System.out.println("Umgeplant: " + this);
		}

		@Override
		public void alarm() {
			Assert.assertFalse(wecker.isWeckerGestelltFuer(this));
			final long jetzt = System.currentTimeMillis();

			System.out.println("Objekt " + this + " geweckt: " + jetzt + ", Differenz: "
					+ ((jetzt - weckZeitPunkt) >= 0 ? "+" : "-") + (jetzt - weckZeitPunkt));
			Assert.assertTrue("Kein Weckzeitpunkt eingeplant", weckZeitPunkt != -1);
			Assert.assertTrue(
					"Alarm erwartet um: " + weckZeitPunkt + ", war aber " + jetzt + " (Differenz: "
							+ Math.abs(jetzt - weckZeitPunkt) + ")",
							Math.abs(jetzt - weckZeitPunkt) < TestWeckObjekt.TOLERANZ);

			if (geweckteObjekte.get(this) == null) {
				geweckteObjekte.put(this, 1);
			} else {
				geweckteObjekte.put(this, geweckteObjekte.get(this) + 1);
			}

			if ((geweckteObjekte.get(this) != null) && (geweckteObjekte.get(this) == 0)) {
				geweckteObjekte.remove(this);
			}
		}

		@Override
		public String toString() {
			return name + " (" + new Long(weckZeitPunkt).toString() + "ms)";
		}

	}
}
