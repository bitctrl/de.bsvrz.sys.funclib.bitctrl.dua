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

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Allgemeine Klasse f�r die Beschreibung von Objekten, die <b>nur</b> Daten
 * halten, auf welche �ber Getter-Methoden (<b>ohne Argumente</b>) zugegriffen
 * werden kann. (z.B. Attributgruppeninhalte)
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class AllgemeinerDatenContainer {

	/**
	 * Vergleicht dieses Objekt mit dem �bergebenen Objekt. Die beiden Objekte
	 * sind dann gleich, wenn sie vom selben Typ sind und wenn alle
	 * Getter-Methoden die gleichen Werte zur�ckliefern.
	 *
	 * @param that
	 *            ein anderes Objekt
	 * @return ob die beiden Objekte inhaltlich gleich sind
	 */
	@Override
	public boolean equals(final Object that) {
		if (that != null) {
			if (that.getClass().equals(this.getClass())) {
				for (final Method method : this.getClass().getMethods()) {
					if (method.getName().startsWith("get")) {
						final Object thisInhalt;
						final Object thatInhalt;

						try {
							thisInhalt = method.invoke(this, new Object[0]);
							thatInhalt = method.invoke(that, new Object[0]);
							if (!thisInhalt.equals(thatInhalt)) {
								return false;
							}
						} catch (final IllegalAccessException e) {
							Debug.getLogger().error(Constants.EMPTY_STRING, e);
							e.printStackTrace();
							return false;
						} catch (final InvocationTargetException e) {
							Debug.getLogger().error(Constants.EMPTY_STRING, e);
							e.printStackTrace();
							return false;
						}
					}
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Kein hashCode() ermittelbar");
	}

	/**
	 * Erfragt eine Zeichenkette, welche die aktuellen Werte aller �ber
	 * Getter-Methoden zug�nglichen Member-Variable enth�lt.
	 *
	 * @return eine Inhaltsangabe dieses Objektes
	 */
	@Override
	public String toString() {
		String s = Constants.EMPTY_STRING;

		for (final Method methode : this.getClass().getMethods()) {
			if (methode.getName().startsWith("get") && methode.getDeclaringClass().equals(this.getClass())) {
				s += methode.getName().substring(3) + " = ";
				try {
					s += methode.invoke(this, new Object[0]);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
					s += "unbekannt";
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
					s += "unbekannt";
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
					s += "unbekannt";
				}
				s += "\n";
			} else if (methode.getName().startsWith("is") && methode.getDeclaringClass().equals(this.getClass())) {
				s += methode.getName().substring(2) + " = ";
				try {
					s += methode.invoke(this, new Object[0]);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
					s += "unbekannt";
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
					s += "unbekannt";
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
					s += "unbekannt";
				}
				s += "\n";

			}
		}

		return s;
	}
}
