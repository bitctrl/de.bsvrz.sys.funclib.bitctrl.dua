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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Allgemeine Klasse für die Beschreibung von Objekten, die <b>nur</b> Daten
 * halten, auf welche über Getter-Methoden (<b>ohne Argumente</b>) zugegriffen
 * werden kann. (z.B. Attributgruppeninhalte)
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @deprecated die Klasse sollte nicht mehr verwendet werden, weil der
 *             API-Kontrakt für die Implementierung von equals und hashCode
 *             verletzt wird und auch nicht sinnvoll implementiert werden kann.
 *             Für die hashCode-Methode wird hier die hashCode-Methode der
 *             Superklasse aufgerufen und eine entsprechende Fehlermeldung
 *             ausgegeben. Die Verwendung der Klasse in Set und Maps als Key
 *             führt aber zu undefiniertem Verhalten.
 */
@Deprecated
public class AllgemeinerDatenContainer {

	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Vergleicht dieses Objekt mit dem übergebenen Objekt. Die beiden Objekte
	 * sind dann gleich, wenn sie vom selben Typ sind und wenn alle
	 * Getter-Methoden die gleichen Werte zurückliefern.
	 *
	 * FIXME equals und hashCode verletzt den API-Kontrakt und sollte hier
	 * entfernt bzw. überdacht werden.
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
							LOGGER.error(Constants.EMPTY_STRING, e);
							e.printStackTrace();
							return false;
						} catch (final InvocationTargetException e) {
							LOGGER.error(Constants.EMPTY_STRING, e);
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
		LOGGER.error("Das Objekt wird ohne korrekte HashCode-Equals-Implementierung verwendet");
		return super.hashCode();
	}

	/**
	 * Erfragt eine Zeichenkette, welche die aktuellen Werte aller über
	 * Getter-Methoden zugänglichen Member-Variable enthält.
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
