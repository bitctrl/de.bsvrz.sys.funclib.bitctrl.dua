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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.NonMutableSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit einem Objekt vom Typ
 * <code>typ.umfeldDatenMessStelle</code> und stellt alle Konfigurationsdaten
 * zur Verfuegung.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class DUAUmfeldDatenMessStelle {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static Map<SystemObject, DUAUmfeldDatenMessStelle> instanzen;

	/**
	 * das Systemobjekt.
	 */
	private final SystemObject objekt;

	/**
	 * Mapt die Umfelddatensensoren dieser Messstelle auf deren
	 * Umfelddatenarten.
	 */
	private final Map<UmfeldDatenArt, DatenSensorMenge> sensoren = new HashMap<>();

	/**
	 * Initialisiert alle Messstellen, die mit den uebergebenen Objekten
	 * assoziiert sind.
	 *
	 * @param dav
	 *            die Datenverteiler-Verbindung
	 * @param messStellenObjekte
	 *            Menge der zu initialisierenden Objekte (muss
	 *            <code>!= null</code> sein)
	 */
	public static void initialisiere(final ClientDavInterface dav, final Collection<SystemObject> messStellenObjekte) {
		if (messStellenObjekte == null) {
			throw new NullPointerException("Menge der Umfelddaten-Messstellen ist <<null>>");
		}
		if (DUAUmfeldDatenMessStelle.instanzen != null) {
			Debug.getLogger().error("UFD-Modell darf nur einmal initialisiert werden");
		}

		DUAUmfeldDatenMessStelle.instanzen = new HashMap<>();
		for (final SystemObject mStObj : messStellenObjekte) {
			DUAUmfeldDatenMessStelle.instanzen.put(mStObj, new DUAUmfeldDatenMessStelle(dav, mStObj));
		}
	}

	/**
	 * Erfragt die statischen Instanzen dieser Klasse.<br>
	 * <b>Achtung:</b> <code>initialisiere(final ClientDavInterface dav,
	 * final SystemObject[] messStellenObjekte)</code> muss vorher aufgerufen
	 * worden sein
	 *
	 * @return die statischen Instanzen dieser Klasse (ggf. leere Liste)
	 */
	public static Collection<DUAUmfeldDatenMessStelle> getInstanzen() {
		if (DUAUmfeldDatenMessStelle.instanzen == null) {
			throw new RuntimeException("DUAUmfeldDatenMessStelle wurde noch nicht initialisiert");
		}

		return DUAUmfeldDatenMessStelle.instanzen.values();
	}

	/**
	 * Erfragt die statische Instanz dieser Klasse, die mit dem uebergebenen
	 * Systemobjekt assoziiert ist.<br>
	 * <b>Achtung:</b> <code>initialisiere(final ClientDavInterface dav,
	 * final SystemObject[] messStellenObjekte)</code> muss vorher aufgerufen
	 * worden sein
	 *
	 * @param messStellenObjekt
	 *            ein Systemobjekt einer Umfelddatenmessstelle
	 * @return die statische Instanz dieser Klasse, die mit dem uebergebenen
	 *         Systemobjekt assoziiert ist oder <code>null</code>, wenn keine
	 *         Instanz gefunden wurde
	 */
	public static DUAUmfeldDatenMessStelle getInstanz(final SystemObject messStellenObjekt) {
		if (DUAUmfeldDatenMessStelle.instanzen == null) {
			throw new RuntimeException("DUAUmfeldDatenMessStelle wurde noch nicht initialisiert");
		}

		return DUAUmfeldDatenMessStelle.instanzen.get(messStellenObjekt);
	}

	/**
	 * Standardkonstruktor.<br>
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            das Systemobjekt der Messstelle
	 */
	private DUAUmfeldDatenMessStelle(final ClientDavInterface dav, final SystemObject objekt) {
		if (objekt == null) {
			throw new NullPointerException("Systemobjekt der Umfelddaten-Messstelle ist <<null>>");
		}
		this.objekt = objekt;

		final Map<UmfeldDatenArt, Set<DUAUmfeldDatenSensor>> datenArtAufSensoren = new HashMap<>();
		for (final UmfeldDatenArt datenArt : UmfeldDatenArt.getInstanzen()) {
			datenArtAufSensoren.put(datenArt, new HashSet<DUAUmfeldDatenSensor>());
		}

		final NonMutableSet sensorenMenge = ((ConfigurationObject) objekt).getNonMutableSet("UmfeldDatenSensoren");
		for (final SystemObject sensorObj : sensorenMenge.getElements()) {
			if (sensorObj.isValid()) {
				final DUAUmfeldDatenSensor sensor;
				try {
					sensor = DUAUmfeldDatenSensor.getInstanz(dav, sensorObj);
				} catch (final UmfeldDatenSensorUnbekannteDatenartException ex) {
					Debug.getLogger().warning("UmfeldDatenMessStelle '" + getObjekt()
					+ "': Umfelddatensensor wird nicht verarbeitet: " + ex.getMessage());
					continue;
				}

				final Set<DUAUmfeldDatenSensor> sensorenMitDatenArt = datenArtAufSensoren.get(sensor.getDatenArt());
				sensorenMitDatenArt.add(sensor);
			}
		}

		for (final UmfeldDatenArt datenArt : UmfeldDatenArt.getInstanzen()) {
			sensoren.put(datenArt, new DatenSensorMenge(datenArtAufSensoren.get(datenArt)));
		}
	}

	/**
	 * Erfragt alle Umfelddatensensoren dieser Messstelle.
	 *
	 * @return alle Umfelddatensensoren dieser Messstelle (ggf. leere Liste)
	 */
	public Collection<DUAUmfeldDatenSensor> getSensoren() {
		final Collection<DUAUmfeldDatenSensor> alleSensoren = new HashSet<>();

		for (final UmfeldDatenArt datenArt : UmfeldDatenArt.getInstanzen()) {
			alleSensoren.addAll(sensoren.get(datenArt).getNebenSensoren());
			if (sensoren.get(datenArt).getHauptSensor() != null) {
				alleSensoren.add(sensoren.get(datenArt).getHauptSensor());
			}
		}

		return alleSensoren;
	}

	/**
	 * Erfragt alle an dieser Umfelddatenmessstelle konfigurierten Sensoren mit
	 * der uebergebenen Datenart.
	 *
	 * @param datenArt
	 *            eine Umfelddatenart
	 * @return alle an dieser Umfelddatenmessstelle konfigurierten Sensoren mit
	 *         der uebergebenen Datenart (ggf. leere Liste)
	 */
	public Collection<DUAUmfeldDatenSensor> getSensoren(final UmfeldDatenArt datenArt) {
		final Collection<DUAUmfeldDatenSensor> alleSensorenDerDatenArt = new HashSet<>();

		alleSensorenDerDatenArt.addAll(sensoren.get(datenArt).getNebenSensoren());
		if (sensoren.get(datenArt).getHauptSensor() != null) {
			alleSensorenDerDatenArt.add(sensoren.get(datenArt).getHauptSensor());
		}

		return alleSensorenDerDatenArt;
	}

	/**
	 * Erfragt den an dieser Umfelddatenmessstelle konfigurierten Hauptsensor
	 * mit der uebergebenen Datenart.
	 *
	 * @param datenArt
	 *            eine Umfelddatenart
	 * @return den an dieser Umfelddatenmessstelle konfigurierten Hauptsensor
	 *         mit der uebergebenen Datenart (ggf. <code>null</code>)
	 */
	public DUAUmfeldDatenSensor getHauptSensor(final UmfeldDatenArt datenArt) {
		return sensoren.get(datenArt).getHauptSensor();
	}

	/**
	 * Erfragt alle an dieser Umfelddatenmessstelle konfigurierten Nebensensoren
	 * mit der uebergebenen Datenart.
	 *
	 * @param datenArt
	 *            eine Umfelddatenart
	 * @return alle an dieser Umfelddatenmessstelle konfigurierten Nebensensoren
	 *         mit der uebergebenen Datenart (ggf. leere Liste)
	 */
	public Collection<DUAUmfeldDatenSensor> getNebenSensoren(final UmfeldDatenArt datenArt) {
		return sensoren.get(datenArt).getNebenSensoren();
	}

	@Override
	public boolean equals(final Object obj) {
		boolean ergebnis = false;

		if ((obj != null) && (obj instanceof DUAUmfeldDatenMessStelle)) {
			final DUAUmfeldDatenMessStelle that = (DUAUmfeldDatenMessStelle) obj;
			ergebnis = objekt.equals(that.objekt);
		}

		return ergebnis;
	}

	@Override
	public String toString() {
		String s = objekt.toString() + "\n";

		for (final UmfeldDatenArt datenArt : UmfeldDatenArt.getInstanzen()) {
			if (!sensoren.get(datenArt).isEmpty()) {
				s += "Datenart: " + datenArt + "\nHS: " + (sensoren.get(datenArt).getHauptSensor() == null ? "keiner"
						: sensoren.get(datenArt).getHauptSensor());
				if (sensoren.get(datenArt).getNebenSensoren().size() != 0) {
					for (final DUAUmfeldDatenSensor nebenSensor : sensoren.get(datenArt).getNebenSensoren()) {
						s += "\nNS: " + nebenSensor;
					}
				} else {
					s += "\nNS: keine";
				}
			}
		}

		return s;
	}

	/**
	 * Erfragt das assoziierte Systemobjekt.
	 *
	 * @return das assoziierte Systemobjekt
	 */
	public SystemObject getObjekt() {
		return objekt;
	}

	/**
	 * Speichert die Umfelddatensensoren einer Messstelle fuer <b>eine</b>
	 * bestimmte Umfelddatenart.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	private class DatenSensorMenge {

		/**
		 * der Hauptsensor.
		 */
		private DUAUmfeldDatenSensor hauptSensor;

		/**
		 * alle Nebensensoren.
		 */
		private final Collection<DUAUmfeldDatenSensor> nebenSensoren = new HashSet<>();

		/**
		 * Standardkonstruktor.
		 *
		 * @param alleSensoren
		 *            Liste aller Sensoren (muss <code>!= null</code> sein)
		 */
		protected DatenSensorMenge(final Set<DUAUmfeldDatenSensor> alleSensoren) {
			for (final DUAUmfeldDatenSensor sensor : alleSensoren) {
				if (sensor.isHauptSensor()) {
					if (hauptSensor != null) {
						throw new RuntimeException("Es darf nur ein Hauptsensor pro Messstelle konfiguriert sein "
								+ objekt + " Sensor: " + hauptSensor + "/" + sensor);
					}
					hauptSensor = sensor;
				} else {
					nebenSensoren.add(sensor);
				}
			}
		}

		/**
		 * Erfragt den Hauptsensor.
		 *
		 * @return den Hauptsensor (ggf. <code>null</code>)
		 */
		protected final DUAUmfeldDatenSensor getHauptSensor() {
			return hauptSensor;
		}

		/**
		 * Erfragt alle Nebensensoren.
		 *
		 * @return alle Nebensensoren (ggf. leere Liste)
		 */
		protected final Collection<DUAUmfeldDatenSensor> getNebenSensoren() {
			return nebenSensoren;
		}

		/**
		 * Erfragt, ob die Menge der in diesem Objekt referenzierten
		 * Umfelddatensensoren leer ist.
		 *
		 * @return ob die Menge der in diesem Objekt referenzierten
		 *         Umfelddatensensoren leer ist
		 */
		protected final boolean isEmpty() {
			return (hauptSensor == null) && nebenSensoren.isEmpty();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((objekt == null) ? 0 : objekt.hashCode());
		return result;
	}
}
