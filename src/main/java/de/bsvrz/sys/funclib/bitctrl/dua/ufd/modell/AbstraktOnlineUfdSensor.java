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

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Allgemeiner Umfelddatensensor mit aktuellen Daten.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <G>
 *            die Daten
 */
public abstract class AbstraktOnlineUfdSensor<G> implements ClientReceiverInterface {

	/**
	 * Beobachterobjekte.
	 */
	private final Set<IOnlineUfdSensorListener<G>> listenerMenge = Collections
			.synchronizedSet(new HashSet<IOnlineUfdSensorListener<G>>());

	/**
	 * das Systemobjekt.
	 */
	private SystemObject objekt;

	/**
	 * aktueller Onlinewert.
	 */
	private G onlineWert;

	/**
	 * Initialisiert dieses Objekt.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt1
	 *            ein Systemobjekt eines Umfelddatensensors (muss
	 *            <code>!= null</code> sein)
	 * @param aspekt
	 *            der Aspekt, aus dem die aktuellen Daten entnommen werden
	 *            sollen
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	protected void initialisiere(final ClientDavInterface dav, final SystemObject objekt1, final Aspect aspekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt1);
		this.objekt = objekt1;

		final DataDescription datenBeschreibung = new DataDescription(
				dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), aspekt);

		dav.subscribeReceiver(this, objekt, datenBeschreibung, ReceiveOptions.normal(), ReceiverRole.receiver());
	}

	/**
	 * Berechnet aus dem aktuellen Sensor-Resultat einen Wert, der von dieser
	 * Klasse den Listenern zur Verfuegung gestellt wird .
	 *
	 * @param resultat
	 *            aktuelles Resultat
	 */
	protected abstract void berechneOnlineWert(final ResultData resultat);

	@Override
	public void update(final ResultData[] resultate) {
		if (resultate != null) {
			for (final ResultData resultat : resultate) {
				if (resultat != null) {
					synchronized (this) {
						this.berechneOnlineWert(resultat);
						if (this.onlineWert != null) {
							for (final IOnlineUfdSensorListener<G> listener : this.listenerMenge) {
								listener.aktualisiereDaten(this.onlineWert);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Fuegt diesem Umfelddatensensor einen Beobachter hinzu.
	 *
	 * @param listener
	 *            ein Beobachter
	 * @param informiereInitial
	 *            zeigt an, ob der Beobachter initial ueber das letzte
	 *            empfangene Datum informiert werden soll (so ueberhaupt schon
	 *            eines empfangen wurde)
	 */
	public final void addListener(final IOnlineUfdSensorListener<G> listener, final boolean informiereInitial) {
		if (informiereInitial) {
			synchronized (this) {
				if (this.listenerMenge.add(listener) && (this.onlineWert != null)) {
					listener.aktualisiereDaten(this.onlineWert);
				}
			}
		} else {
			synchronized (this) {
				this.listenerMenge.add(listener);
			}
		}
	}

	/**
	 * Erfragt das Systemobjekt.
	 *
	 * @return das Systemobjekt
	 */
	public final SystemObject getObjekt() {
		return this.objekt;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean ergebnis = false;

		if ((obj != null) && (obj instanceof AbstraktOnlineUfdSensor)) {
			final AbstraktOnlineUfdSensor<?> that = (AbstraktOnlineUfdSensor<?>) obj;
			ergebnis = this.objekt.equals(that.objekt);
		}

		return ergebnis;
	}

	@Override
	public String toString() {
		return this.objekt.toString();
	}

	protected void setOnlineWert(final G onlineWert) {
		this.onlineWert = onlineWert;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((objekt == null) ? 0 : objekt.hashCode());
		return result;
	}



}
