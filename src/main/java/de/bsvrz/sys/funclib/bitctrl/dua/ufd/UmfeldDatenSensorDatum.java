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

package de.bsvrz.sys.funclib.bitctrl.dua.ufd;

import java.util.Date;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.bitctrl.dua.GanzZahl;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Schnittstelle zu einem Roh-Sensorwert eines Umfelddatensensors <b>mit</b>
 * Plausibilisierungs-Informationen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class UmfeldDatenSensorDatum {

	/**
	 * das empfangene Originaldatum.
	 */
	private final ResultData originalDatum;

	/**
	 * die Art des Umfelddatums.
	 */
	private UmfeldDatenArt datenArt;

	/**
	 * ein DAV-Datum eines Umfelddatensensors.
	 */
	private Data datum;

	/**
	 * Indiziert, ob es sich bei diesem Datum schon um eine modifizierbare Kopie
	 * handelt.
	 */
	private boolean copy;

	/**
	 * der eigentliche Wert des Umfelddatensensors (ohne
	 * Plausibilisierungs-Informationen).
	 */
	private final UmfeldDatenSensorWert wert;

	/**
	 * Standardkonstruktor.
	 *
	 * @param resultat
	 *            ein Roh-Sensorwert eines Umfelddatensensors (
	 *            <code>!= null</code>)
	 */
	public UmfeldDatenSensorDatum(final ResultData resultat) {
		if (resultat == null) {
			throw new NullPointerException("Datensatz ist <<null>>");
		}
		if (resultat.getData() == null) {
			throw new NullPointerException("Datensatz enthaelt keine Daten");
		}

		originalDatum = resultat;
		try {
			datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(resultat.getObject());
		} catch (final UmfeldDatenSensorUnbekannteDatenartException ex) {
			Debug.getLogger().finest(ex.getLocalizedMessage(), ex);
		}

		if (datenArt == null) {
			throw new NullPointerException("Datenart konnte nicht identifiziert werden:\n" + resultat);
		}

		datum = resultat.getData();
		wert = new UmfeldDatenSensorWert(datenArt);
		wert.setWert(datum.getItem(datenArt.getName()).getUnscaledValue("Wert").longValue());
		wert.setVeraendert(false);
	}

	/**
	 * Erfragt ein <code>ResultData</code>-Objekt, mit dem Datensatz, wie er
	 * sich jetzt gerade in diesem Objekt befindet.
	 *
	 * @return ein <code>ResultData</code>-Objekt, das mit den aktuellen Daten
	 *         dieses Objekts korrespondiert
	 */
	public final ResultData getVeraendertesOriginalDatum() {
		ResultData resultat = originalDatum;

		if (copy) {
			resultat = new ResultData(originalDatum.getObject(), originalDatum.getDataDescription(),
					originalDatum.getDataTime(), datum);
		}

		return resultat;
	}

	/**
	 * Erfragt, ob dieses Datum verändert wurde.
	 *
	 * @return ob dieses Datum verändert wurde
	 */
	public final boolean isVeraendert() {
		return copy;
	}

	/**
	 * Erstellt eine Kopie des hier verarbeiteten Datums (wenn dies nicht schon
	 * vorher passiert ist).
	 */
	private void erstelleKopie() {
		if (!copy) {
			copy = true;
			datum = datum.createModifiableCopy();
		}
	}

	/**
	 * Erfragt das originale <code>ResultData</code>, mit dem diese Instanz
	 * initialisiert wurde.
	 *
	 * @return das originale <code>ResultData</code>
	 */
	public final ResultData getOriginalDatum() {
		return originalDatum;
	}

	/**
	 * Erfragt den Gueteindex.
	 *
	 * @return der Gueteindex
	 */
	public final GanzZahl getGueteIndex() {
		final GanzZahl gueteIndex = GanzZahl.getGueteIndex();

		gueteIndex.setWert(datum.getItem(datenArt.getName()).getItem("Güte").getUnscaledValue("Index").longValue());

		return gueteIndex;
	}

	/**
	 * Erfragt das Gueteverfahren.
	 *
	 * @return das Gueteverfahren
	 */
	public final int getGueteVerfahren() {
		final int gueteVerfahren = datum.getItem(datenArt.getName()).getItem("Güte").getUnscaledValue("Verfahren")
				.intValue();

		return gueteVerfahren;
	}

	/**
	 * Erfragt das Erfassungsintervall dieses Datums.
	 *
	 * @return das Erfassungsintervall dieses Datums
	 */
	public final long getT() {
		return datum.getTimeValue("T").getMillis();
	}

	/**
	 * Setzt das Erfassungsintervall dieses Datums.
	 *
	 * @param t
	 *            das Erfassungsintervall dieses Datums
	 */
	public final void setT(final long t) {
		erstelleKopie();
		datum.getTimeValue("T").setMillis(t);
	}

	/**
	 * Erfragt den Wert <code>Status.Erfassung.NichtErfasst</code>.
	 *
	 * @return der Wert <code>Status.Erfassung.NichtErfasst</code>
	 */
	public final int getStatusErfassungNichtErfasst() {
		return datum.getItem(datenArt.getName()).getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst")
				.intValue();
	}

	/**
	 * Setzt den Wert <code>Status.Erfassung.NichtErfasst</code>.
	 *
	 * @param statusErfassungNichtErfasst
	 *            der Wert <code>Status.Erfassung.NichtErfasst</code>
	 */
	public final void setStatusErfassungNichtErfasst(final int statusErfassungNichtErfasst) {
		erstelleKopie();
		datum.getItem(datenArt.getName()).getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst")
		.set(statusErfassungNichtErfasst);
	}

	/**
	 * Setzte den Gueteindex.
	 *
	 * @param guete
	 *            der neue Gueteindex
	 */
	public final void setGueteIndex(final long guete) {
		erstelleKopie();

		datum.getItem(datenArt.getName()).getItem("Güte").getUnscaledValue("Index").set(guete);
	}

	/**
	 * Setzte den Gueteindex.
	 *
	 * @param gueteVerfahren
	 *            der neue Gueteindex
	 */
	public final void setGueteVerfahren(final int gueteVerfahren) {
		erstelleKopie();

		datum.getItem(datenArt.getName()).getItem("Güte").getUnscaledValue("Verfahren").set(gueteVerfahren);
	}

	/**
	 * Erfragt den Wert <code>Status.MessWertErsetzung.Implausibel</code>.
	 *
	 * @return der Wert <code>Status.MessWertErsetzung.Implausibel</code>
	 */
	public final int getStatusMessWertErsetzungImplausibel() {
		return datum.getItem(datenArt.getName()).getItem("Status").getItem("MessWertErsetzung")
				.getUnscaledValue("Implausibel").intValue();
	}

	/**
	 * Erfragt den Wert <code>Status.MessWertErsetzung.Interpoliert</code>.
	 *
	 * @return der Wert <code>Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final int getStatusMessWertErsetzungInterpoliert() {
		return datum.getItem(datenArt.getName()).getItem("Status").getItem("MessWertErsetzung")
				.getUnscaledValue("Interpoliert").intValue();
	}

	/**
	 * Setzt den Wert <code>Status.MessWertErsetzung.Implausibel</code>.
	 *
	 * @param statusMessWertErsetzungImplausibel
	 *            der Wert <code>Status.MessWertErsetzung.Implausibel</code>
	 */
	public final void setStatusMessWertErsetzungImplausibel(final int statusMessWertErsetzungImplausibel) {
		erstelleKopie();
		datum.getItem(datenArt.getName()).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel")
		.set(statusMessWertErsetzungImplausibel);
	}

	/**
	 * Setzt den Wert <code>Status.MessWertErsetzung.Interpoliert</code>.
	 *
	 * @param statusMessWertErsetzungInterpoliert
	 *            der Wert <code>Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final void setStatusMessWertErsetzungInterpoliert(final int statusMessWertErsetzungInterpoliert) {
		erstelleKopie();
		datum.getItem(datenArt.getName()).getItem("Status").getItem("MessWertErsetzung")
		.getUnscaledValue("Interpoliert").set(statusMessWertErsetzungInterpoliert);
	}

	/**
	 * Erfragt den Wert selbst.
	 *
	 * @return der Sensor-Messwert
	 */
	public final UmfeldDatenSensorWert getWert() {
		return wert;
	}

	/**
	 * Erfragt die Datenzeit dieses Datums.
	 *
	 * @return die Datenzeit dieses Datums
	 */
	public final long getDatenZeit() {
		return originalDatum.getDataTime();
	}

	/**
	 * Erfragt das mit dem aktuellen Zustand dieses Objektes assoziierte
	 * DAV-Datum.
	 *
	 * @return das mit dem aktuellen Zustand dieses Objektes assoziierte
	 *         DAV-Datum
	 */
	public final Data getDatum() {
		if (wert.isVeraendert()) {
			erstelleKopie();
			datum.getItem(datenArt.getName()).getUnscaledValue("Wert").set(wert.getWert());
		}
		return datum;
	}

	@Override
	public String toString() {
		String s = datenArt.toString();

		s += "\nKopiert: " + (copy ? "ja" : "nein");
		s += "\nDatum: " + datum;
		s += "\nDatenzeit: " + new Date(getDatenZeit());
		s += "\nWert: " + wert;

		return s;
	}

}
