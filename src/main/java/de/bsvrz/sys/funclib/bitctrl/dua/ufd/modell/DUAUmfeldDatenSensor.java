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

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Korrespondiert mit einem Objekt vom Typ <code>typ.umfeldDatenSensor</code>
 * und stellt alle Konfigurationsdaten, sowie die Parameter der
 * Messwertersetzung zur Verfügung.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DUAUmfeldDatenSensor implements ClientReceiverInterface {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static Map<SystemObject, DUAUmfeldDatenSensor> instanzen = new HashMap<>();

	/**
	 * das Systemobjekt.
	 */
	private final SystemObject objekt;

	/**
	 * Maximaler Zeitbereich, über den eine Messwertersetzung für diesen Sensor
	 * durchgeführt wird.
	 */
	private long maxZeitMessWertErsetzung = -1;

	/**
	 * Maximaler Zeitbereich, über den eine Messwertfortschreibung bei
	 * implausiblen Werten stattfindet.
	 */
	private long maxZeitMessWertFortschreibung = -1;

	/**
	 * Die Umfelddatenmessstelle vorher.
	 */
	private SystemObject vorgaenger;

	/**
	 * Die Umfelddatenmessstelle nachher.
	 */
	private SystemObject nachfolger;

	/**
	 * Ersatzsensor dieses Umfelddatensensors für die Messwertersetzung.
	 */
	private SystemObject ersatzSensor;

	/**
	 * Zeigt an, ob dieser Sensor der Hauptsensor für diesen Sensortyp an der
	 * Umfelddatenmessstelle, oder ein(er von mehreren) Nebensensoren für diesen
	 * Sensortyp an der Umfelddatenmessstelle ist.
	 */
	private boolean hauptSensor;

	/**
	 * Die Umfelddatenart dieses Sensors.
	 */
	private final UmfeldDatenArt datenArt;

	/**
	 * Erfragt die statische Instanz dieser Klasse, die mit dem uebergebenen
	 * Systemobjekt assoziiert ist (nicht vorhandene werden ggf. angelegt)
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors
	 * @return die statische Instanz dieser Klasse, die mit dem uebergebenen
	 *         Systemobjekt assoziiert ist
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	static final DUAUmfeldDatenSensor getInstanz(final ClientDavInterface dav, final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException("Umfelddatensensor mit Systemobjekt <<null>> existiert nicht");
		}

		DUAUmfeldDatenSensor instanz = DUAUmfeldDatenSensor.instanzen.get(objekt);

		if (instanz == null) {
			instanz = new DUAUmfeldDatenSensor(dav, objekt);
			DUAUmfeldDatenSensor.instanzen.put(objekt, instanz);
		}

		return instanz;
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            das Systemobjekt des Umfelddatensensors
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 *             der Sensor hat keinen der unterstützten Typen
	 */
	protected DUAUmfeldDatenSensor(final ClientDavInterface dav, final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException("Als Umfelddatensensor wurde <<null>> uebergeben");
		}
		this.objekt = objekt;

		datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);
		if (datenArt == null) {
			throw new UmfeldDatenSensorUnbekannteDatenartException("Datenart von Umfelddatensensor " + this.objekt
					+ " (" + objekt.getType() + ") konnte nicht identifiziert werden");
		}

		final ConfigurationObject konfigObjekt = (ConfigurationObject) objekt;
		final Data konfigDaten = konfigObjekt
				.getConfigurationData(dav.getDataModel().getAttributeGroup("atg.umfeldDatenSensor"));

		if (konfigDaten != null) {
			if (konfigDaten.getReferenceValue("Vorgänger") != null) {
				vorgaenger = konfigDaten.getReferenceValue("Vorgänger").getSystemObject();
			}
			if (konfigDaten.getReferenceValue("Nachfolger") != null) {
				nachfolger = konfigDaten.getReferenceValue("Nachfolger").getSystemObject();
			}

			if (konfigDaten.getReferenceValue("ErsatzSensor") != null) {
				ersatzSensor = konfigDaten.getReferenceValue("ErsatzSensor").getSystemObject();
			}
			hauptSensor = konfigDaten.getUnscaledValue("Typ").intValue() == 0;
		}

		final DataDescription parameterBeschreibung = new DataDescription(
				dav.getDataModel().getAttributeGroup("atg.ufdsMessWertErsetzung"),
				dav.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
		dav.subscribeReceiver(this, objekt, parameterBeschreibung, ReceiveOptions.normal(), ReceiverRole.receiver());
	}

	/**
	 * Erfragt die Umfelddatenart dieses Sensors.
	 *
	 * @return die Umfelddatenart dieses Sensors
	 */
	public final UmfeldDatenArt getDatenArt() {
		return datenArt;
	}

	/**
	 * Erfragt die Umfelddatenmessstelle vorher.
	 *
	 * @return die Umfelddatenmessstelle vorher oder <code>null</code>, wenn
	 *         diese nicht konfiguriert ist
	 */
	public final SystemObject getVorgaenger() {
		return vorgaenger;
	}

	/**
	 * Ergagt die Umfelddatenmessstelle nachher.
	 *
	 * @return die Umfelddatenmessstelle nachher oder <code>null</code>, wenn
	 *         diese nicht konfiguriert ist
	 */
	public final SystemObject getNachfolger() {
		return nachfolger;
	}

	/**
	 * Erfragt den Ersatzsensor dieses Umfelddatensensors für die
	 * Messwertersetzung.
	 *
	 * @return der Ersatzsensor dieses Umfelddatensensors für die
	 *         Messwertersetzung oder <code>null</code>, wenn dieser nicht
	 *         konfiguriert ist
	 */
	public final SystemObject getErsatzSensor() {
		return ersatzSensor;
	}

	/**
	 * Erfragt, ob dieser Sensor der Hauptsensor für diesen Sensortyp an der
	 * Umfelddatenmessstelle, oder ein(er von mehreren) Nebensensoren für diesen
	 * Sensortyp an der Umfelddatenmessstelle ist.
	 *
	 * @return ob dieser Sensor der Hauptsensor ist
	 */
	public final boolean isHauptSensor() {
		return hauptSensor;
	}

	@Override
	public void update(final ResultData[] resultate) {
		if (resultate != null) {
			for (final ResultData resultat : resultate) {
				if ((resultat != null) && (resultat.getData() != null)) {
					final Data ufdsMessWertErsetzungData = resultat.getData();
					maxZeitMessWertErsetzung = ufdsMessWertErsetzungData.getTimeValue("maxZeitMessWertErsetzung")
							.getMillis();

					maxZeitMessWertFortschreibung = ufdsMessWertErsetzungData
							.getTimeValue("maxZeitMessWertFortschreibung").getMillis();
				}
			}
		}
	}

	/**
	 * Erfragt den maximalen Zeitbereich, über den eine Messwertersetzung für
	 * diesen Sensor durchgeführt wird.
	 *
	 * @return maximaler Zeitbereich, über den eine Messwertersetzung für diesen
	 *         Sensor durchgeführt wird
	 */
	public final long getMaxZeitMessWertErsetzung() {
		return maxZeitMessWertErsetzung;
	}

	/**
	 * Erfragt den maximalen Zeitbereich, über den eine Messwertfortschreibung
	 * bei implausiblen Werten stattfindet.
	 *
	 * @return maximaler Zeitbereich, über den eine Messwertfortschreibung bei
	 *         implausiblen Werten stattfindet
	 */
	public final long getMaxZeitMessWertFortschreibung() {
		return maxZeitMessWertFortschreibung;
	}

	/**
	 * Erfragt das Systemobjekt.
	 *
	 * @return das Systemobjekt
	 */
	public final SystemObject getObjekt() {
		return objekt;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean ergebnis = false;

		if ((obj != null) && (obj instanceof DUAUmfeldDatenSensor)) {
			final DUAUmfeldDatenSensor that = (DUAUmfeldDatenSensor) obj;
			ergebnis = objekt.equals(that.objekt);
		}

		return ergebnis;
	}

	@Override
	public String toString() {
		return objekt.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((objekt == null) ? 0 : objekt.hashCode());
		return result;
	}

	/**
	 * entfernt die gespeicherten Instanzen für Testzwecke.
	 *
	 * TODO prüfen, wozu das gut ist!
	 */
	protected static void resetCache() {
		DUAUmfeldDatenSensor.instanzen.clear();
	}

}
