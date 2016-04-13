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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IKontrollProzessListener;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IObjektWeckerListener;

/**
 * Eine Instanz dieser Klasse kann eine Menge von Objekten zu bestimmten
 * Zeitpunkten über die Schnittstelle <code>IObjektWeckerListener</code>
 * alarmieren.<br>
 * <b>Achtung</b>: Für jedes Objekt kann nur ein Weckzeitpunkt eingestellt
 * werden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class ObjektWecker implements IKontrollProzessListener<Long> {

	/**
	 * Weckzeitpunkt <code><b>AUS</b></code>.
	 */
	public static final long AUS = -1;

	/**
	 * Interner Kontrollprozess.
	 */
	protected KontrollProzess<Long> kontrollProzess = new KontrollProzess<>();

	/**
	 * Mapt alle Weckzeitpunkte auf die Liste der Objekte, die geweckt werden
	 * sollen.
	 */
	protected SortedMap<Long, Set<IObjektWeckerListener>> weckZeitpunktAufObjekte = Collections
			.synchronizedSortedMap(new TreeMap<Long, Set<IObjektWeckerListener>>());

	/**
	 * Mapt alle zu weckenden Objekte auf den Weckzeitpunkt.
	 */
	protected Map<IObjektWeckerListener, Long> objektAufWeckZeitpunkt = Collections
			.synchronizedMap(new HashMap<IObjektWeckerListener, Long>());

	/**
	 * Standardkonstruktor.
	 */
	public ObjektWecker() {
		kontrollProzess.addListener(this);
	}

	/**
	 * Erfragt, ob der Wecker für das übergebene Objekt gestellt ist.
	 *
	 * @param zuWeckendesObjekt
	 *            ein Objekt
	 * @return ob der Wecker für das übergebene Objekt gestellt ist
	 */
	public final boolean isWeckerGestelltFuer(final IObjektWeckerListener zuWeckendesObjekt) {
		boolean weckerGestellt = false;

		synchronized (this) {
			weckerGestellt = objektAufWeckZeitpunkt.get(zuWeckendesObjekt) != null;
		}

		return weckerGestellt;
	}

	/**
	 * Stellt den Wecker für ein Objekt auf einen absoluten Weckzeitpunkt.
	 *
	 * @param zuWeckendesObjekt
	 *            das zu weckende Objekt
	 * @param weckZeitpunkt
	 *            der Weckzeitpunkt (in ms), bzw. <code>AUS</code>
	 */
	public final void setWecker(final IObjektWeckerListener zuWeckendesObjekt, final long weckZeitpunkt) {
		synchronized (this) {
			if (weckZeitpunkt == ObjektWecker.AUS) {
				final Long eingeplanterWeckZeitPunkt = objektAufWeckZeitpunkt.get(zuWeckendesObjekt);

				if (eingeplanterWeckZeitPunkt != null) {
					objektAufWeckZeitpunkt.remove(zuWeckendesObjekt);
					final Set<IObjektWeckerListener> objekte = weckZeitpunktAufObjekte.get(eingeplanterWeckZeitPunkt);
					if (objekte != null) {
						objekte.remove(zuWeckendesObjekt);
						if (objekte.isEmpty()) {
							weckZeitpunktAufObjekte.remove(eingeplanterWeckZeitPunkt);
						}
					}
				}
			} else {
				final Long bisherEingeplanterWeckZeitPunkt = objektAufWeckZeitpunkt.get(zuWeckendesObjekt);

				if (bisherEingeplanterWeckZeitPunkt != null) {
					final Set<IObjektWeckerListener> objekte = weckZeitpunktAufObjekte
							.get(bisherEingeplanterWeckZeitPunkt);
					if (objekte != null) {
						objekte.remove(zuWeckendesObjekt);
						if (objekte.isEmpty()) {
							weckZeitpunktAufObjekte.remove(bisherEingeplanterWeckZeitPunkt);
						}
					}
				}

				Set<IObjektWeckerListener> weckObjekteZumZeitpunkt = weckZeitpunktAufObjekte.get(weckZeitpunkt);

				if (weckObjekteZumZeitpunkt == null) {
					weckObjekteZumZeitpunkt = new HashSet<>();
					weckZeitpunktAufObjekte.put(weckZeitpunkt, weckObjekteZumZeitpunkt);
				}
				weckObjekteZumZeitpunkt.add(zuWeckendesObjekt);
				objektAufWeckZeitpunkt.put(zuWeckendesObjekt, weckZeitpunkt);
			}

			aktualisiereKontrollProzess();
		}
	}

	/**
	 * Aktualisiert den internen Kontrollprozess.
	 */
	protected synchronized void aktualisiereKontrollProzess() {
		if (!weckZeitpunktAufObjekte.isEmpty()) {
			final Long naechsterWeckZeitPunkt = weckZeitpunktAufObjekte.firstKey();
			if (naechsterWeckZeitPunkt != null) {
				kontrollProzess.setNaechstenAufrufZeitpunkt(naechsterWeckZeitPunkt, naechsterWeckZeitPunkt);
			}
		}
	}

	@Override
	public void trigger(final Long weckZeitpunkt) {
		final Set<IObjektWeckerListener> zuWeckendeObjekte = new HashSet<>();

		synchronized (this) {
			final Set<IObjektWeckerListener> menge = weckZeitpunktAufObjekte.get(weckZeitpunkt);
			weckZeitpunktAufObjekte.remove(weckZeitpunkt);
			if (menge == null) {
				return;
			}
			zuWeckendeObjekte.addAll(menge);
		}

		for (final IObjektWeckerListener objekt : zuWeckendeObjekte) {
			synchronized (objektAufWeckZeitpunkt) {
				objektAufWeckZeitpunkt.remove(objekt);
			}
			objekt.alarm();
		}

		aktualisiereKontrollProzess();
	}
}
