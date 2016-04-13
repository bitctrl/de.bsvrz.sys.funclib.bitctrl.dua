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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IKontrollProzessListener;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Instanzen dieser Klasse rufen zu bestimmten Zeitpunkten all ihre Beobachter
 * auf und teilen diesen dann eine bestimmte Information des generischen Typs
 * <code>T</code> mit. Der Zeitpunkt sowie die Information können dabei während
 * der Laufzeit verändert werden
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <T>
 *            Information
 */
public class KontrollProzess<T> {

	/**
	 * der Timer, der den Prozess steuert.
	 */
	private final Timer timer;

	/**
	 * aktueller Prozess.
	 */
	private Prozess prozess;

	/**
	 * nächster Zeitpunkt, zu dem dieser Prozess seine Beobachter informiert.
	 */
	private long naechsterAufrufZeitpunkt = -1;

	/**
	 * ein Objekt mit einer bestimmten Information, das beim nächsten
	 * Aufrufzeitpunkt an alle Beobachterobjekte weitergeleitet wird.
	 */
	protected T aktuelleInformation;

	/**
	 * Menge von Beobachtern, die auf diesen Prozess hören.
	 */
	protected Collection<IKontrollProzessListener<T>> listenerMenge = Collections
			.synchronizedSet(new HashSet<IKontrollProzessListener<T>>());

	/**
	 * Standardkonstruktor.
	 */
	public KontrollProzess() {
		this.timer = new Timer();
		this.prozess = new Prozess();
	}

	/**
	 * Setzt den nächsten Zeitpunkt, zu dem dieser Prozess seine Beobachter
	 * informiert<br>
	 * <b>Achtung:</b> Wenn der nächste Aufrufzeitpunkt in der Vergangenheit
	 * liegt, wird er sofort ausgeführt.
	 *
	 * @param zeitpunktInMillis
	 *            nächster Zeitpunkt, zu dem dieser Prozess seine Beobachter
	 *            informiert
	 */
	public final synchronized void setNaechstenAufrufZeitpunkt(final long zeitpunktInMillis) {
		if (this.naechsterAufrufZeitpunkt != zeitpunktInMillis) {
			Debug.getLogger()
					.info("Der eingeplante Kontrollzeitpunkt wird verändert" + "\nAlt: "
							+ DUAKonstanten.ZEIT_FORMAT_GENAU.format(new Date(this.naechsterAufrufZeitpunkt))
							+ "\nNeu: " + DUAKonstanten.ZEIT_FORMAT_GENAU.format(new Date(zeitpunktInMillis)));
			this.naechsterAufrufZeitpunkt = zeitpunktInMillis;
			this.prozess.cancel();
			this.timer.purge();
			this.prozess = new Prozess();
			timer.schedule(this.prozess, new Date(this.naechsterAufrufZeitpunkt));
		}
	}

	/**
	 * Setzt den nächsten Zeitpunkt, zu dem dieser Prozess seine Beobachter
	 * informiert und übergibt eine Information, die zu diesem Zeitpunkt an alle
	 * Beobachter weitergereicht werden soll. Sollte dieser Zeitpunkt identisch
	 * mit dem bislang eingeplanten Zeitpunkt sein, so werden nur die
	 * Informationen angepasst
	 *
	 * @param zeitpunktInMillis
	 *            nächster Zeitpunkt, zu dem dieser Prozess seine Beobachter
	 *            informiert
	 * @param information
	 *            ein Objekt mit einer bestimmten Information, das beim nächsten
	 *            Aufrufzeitpunkt an alle Beobachterobjekte weitergeleitet wird
	 */
	public final synchronized void setNaechstenAufrufZeitpunkt(final long zeitpunktInMillis, final T information) {
		this.aktuelleInformation = information;
		this.setNaechstenAufrufZeitpunkt(zeitpunktInMillis);
	}

	/**
	 * Erfragt den nächsten Zeitpunkt, zu dem dieser Prozess seine Beobachter
	 * informiert.
	 *
	 * @return nächster Zeitpunkt, zu dem dieser Prozess seine Beobachter
	 *         informiert
	 */
	public final synchronized long getNaechstenAufrufZeitpunkt() {
		return this.naechsterAufrufZeitpunkt;
	}

	/**
	 * Setzt ein Objekt mit einer bestimmten Information, das beim nächsten
	 * Aufrufzeitpunkt an alle Beobachterobjekte weitergeleitet wird.
	 *
	 * @param information
	 *            ein Objekt mit einer bestimmten Information, das beim nächsten
	 *            Aufrufzeitpunkt an alle Beobachterobjekte weitergeleitet wird
	 */
	public final synchronized void setInformation(final T information) {
		this.aktuelleInformation = information;
	}

	/**
	 * Erfragt das Objekt mit einer bestimmten Information, das beim nächsten
	 * Aufrufzeitpunkt an alle Beobachterobjekte weitergeleitet wird.
	 *
	 * @return das Objekt mit einer bestimmten Information, das beim nächsten
	 *         Aufrufzeitpunkt an alle Beobachterobjekte weitergeleitet wird
	 */
	public final synchronized T getInformation() {
		return this.aktuelleInformation;
	}

	/**
	 * Fügt diesem Element einen neuen Beobachter hinzu.
	 *
	 * @param listener
	 *            der neue Beobachter
	 */
	public final void addListener(final IKontrollProzessListener<T> listener) {
		if (listener != null) {
			synchronized (this.listenerMenge) {
				this.listenerMenge.add(listener);
			}
		}
	}

	/**
	 * Löscht ein Beobachterobjekt.
	 *
	 * @param listener
	 *            das zu löschende Beobachterobjekt
	 */
	public final void removeListener(final IKontrollProzessListener<T> listener) {
		if (listener != null) {
			synchronized (this.listenerMenge) {
				this.listenerMenge.remove(listener);
			}
		}
	}

	/**
	 * Prozess, der zu einem bestimmten Zeitpunkt alle Beobachter informiert.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class Prozess extends TimerTask {

		@Override
		public void run() {
			synchronized (KontrollProzess.this.listenerMenge) {
				for (final IKontrollProzessListener<T> listener : KontrollProzess.this.listenerMenge) {
					listener.trigger(KontrollProzess.this.aktuelleInformation);
				}
			}
		}

	}

}
