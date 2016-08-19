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

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueRange;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Umfelddatenart. Verbindet alle notwendigen Informationen (Name, Abkürzung)
 * eines Umfelddaten-Systemobjekttyps mit dem Systemobjekttyp selbst. Diese
 * Klasse muss statisch instanziiert werden, bevor irgendeine Methode daraus das
 * erste Mal benutzt werden kann.<br>
 * z.B.:<br>
 * Typ: typ.ufdsFahrBahnFeuchte --&gt; Name: FahrBahnFeuchte --&gt; Abk: FBF
 *
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public enum UmfeldDatenArt {

	/** Umfelddatenart Fahrbahnfeuchte. */
	fbf("typ.ufdsFahrBahnFeuchte", "FBF", 0),

	/** Umfelddatenart Fahrbahnglätte. */
	fbg("typ.ufdsFahrBahnGlätte", "FBG", 0),

	/** Umfelddatenart Fahrbahnoberflächentemperatur. */
	fbt("typ.ufdsFahrBahnOberFlächenTemperatur", "FBT", -1000),

	/** Umfelddatenart Gefriertemperatur. */
	gt("typ.ufdsGefrierTemperatur", "GT", -1000),

	/** Umfelddatenart Helligkeit. */
	hk("typ.ufdsHelligkeit", "HK", 0),

	/** Umfelddatenart Lufttemperatur. */
	lt("typ.ufdsLuftTemperatur", "LT", -1000),

	/** Umfelddatenart Niederaschalgsart. */
	ns("typ.ufdsNiederschlagsArt", "NS", 0),

	/** Umfelddatenart Niederschlagsintensität. */
	ni("typ.ufdsNiederschlagsIntensität", "NI", 0),

	/** Umfelddatenart Niederschlagsmenge. */
	nm("typ.ufdsNiederschlagsMenge", "NM", 0),

	/** Umfelddatenart Relative Luftfeuchte. */
	rlf("typ.ufdsRelativeLuftFeuchte", "RLF", 0),

	/** Umfelddatenart Schneehöhe. */
	sh("typ.ufdsSchneeHöhe", "SH", 0),

	/** Umfelddatenart Sichtweite. */
	sw("typ.ufdsSichtWeite", "SW", 0),

	/** Umfelddatenart Taupunkttemperatur. */
	tpt("typ.ufdsTaupunktTemperatur", "TPT", -1000),

	/** Umfelddatenart Temperatur in Tiefe 1. */
	tt1("typ.ufdsTemperaturInTiefe1", "TT1", -1000),

	/** Umfelddatenart Temperatur in Tiefe 2. */
	tt2("typ.ufdsTemperaturInTiefe2", "TT2", -1000),

	/** Umfelddatenart Temoeratur in Tiefe 3. */
	tt3("typ.ufdsTemperaturInTiefe3", "TT3", -1000),

	/** Umfelddatenart Wasserfilmdicke. */
	wfd("typ.ufdsWasserFilmDicke", "WFD", 0),

	/** Umfelddatenart Windrichtung. */
	wr("typ.ufdsWindRichtung", "WR", 0),

	/** Umfelddatenart Fahrbahnoberflächenzustand. */
	fbz("typ.ufdsFahrBahnOberFlächenZustand", "FBZ", 0),

	/** Umfelddatenart Luftdruck. */
	ld("typ.ufdsLuftDruck", "LD", 0),

	/** Umfelddatenart Restsalz. */
	rs("typ.ufdsRestSalz", "RS", 0),

	/** Umfelddatenart Taustoffmenge. */
	tsq("typ.ufdsTaustoffmenge", "TSQ", 0),

	/** Umfelddatenart Windgeschwindigkeit (Mittelwert). */
	wgm("typ.ufdsWindGeschwindigkeitMittelWert", "WGM", 0),

	/** Umfelddatenart Windgeschwindigkeit (Spitzenwert). */
	wgs("typ.ufdsWindGeschwindigkeitSpitzenWert", "WGS", 0),

	/** Umfelddatenart Zeitreserve Eisglätte. */
	zrge("typ.ufdsZeitReserveEisGlätte", "ZRGe", 0),

	/** Umfelddatenart Zeitreserve Reifglätte. */
	zrgr("typ.ufdsZeitReserveReifGlätte", "ZRGr", 0),

	/** Umfelddatenart Zeitreserve Glätte Vaisala. */
	zg("typ.ufdsZeitreserveGlätteVaisala", "ZG", 0);

	/**
	 * Mapt den Systemobjekttyp eines Umfelddatensensors auf die Informationen
	 * zu seinem Namen und seiner Abkürzung.
	 */
	private static Map<SystemObjectType, UmfeldDatenArt> typAufArt = new LinkedHashMap<>();

	/**
	 * die PID des Systemobjekt-Typ des Umfelddatensensors.
	 */
	private final String typPid;

	/**
	 * der Systemobjekt-Typ des Umfelddatensensors.
	 */
	private SystemObjectType type;

	/**
	 * der Name der Umfelddatenart.
	 */
	private final String name;

	/**
	 * die Abkürzung der Umfelddatenart.
	 */
	private final String abkuerzung;

	/**
	 * Die Skalierung von Werten dieses Typs im Datenkatalog.
	 */
	private double skalierung = 1.0;

	/**
	 * Offset für die Statuswerte fehlerhaft, nicht ermittelbar und
	 * fehlerhaft/nicht ermittelbar.
	 */
	private long statusOffset;

	/**
	 * Erfragt die Umfelddatenart eines Systemobjekts.
	 *
	 * @param objekt
	 *            die Umfelddatenart eines Systemobjekts oder <code>null</code>,
	 *            wenn es sich nicht um das Systemobjekt eines
	 *            Umfelddatensensors handelt
	 * @return die Umfelddatenart eines Systemobjekts.
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 *             wenn die Datenart nicht bestimmt werden kann (d.h. der Typ
	 *             ist in der Liste der unterstützten Typen nicht enthalten,
	 *             siehe initialisiere()).
	 */
	public static UmfeldDatenArt getUmfeldDatenArtVon(final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		if (UmfeldDatenArt.typAufArt.isEmpty()) {
			throw new RuntimeException("Umfelddatenarten wurden noch nicht initialisiert");
		}

		UmfeldDatenArt umfeldDatenArt = null;

		if (objekt != null) {
			umfeldDatenArt = UmfeldDatenArt.typAufArt.get(objekt.getType());
			if (umfeldDatenArt == null) {
				throw new UmfeldDatenSensorUnbekannteDatenartException("Datenart von Umfelddatensensor " + objekt + " ("
						+ objekt.getType() + ") konnte nicht identifiziert werden");
			}
		} else {
			System.out.println();
			Debug.getLogger().error("Uebergebenes Systemobjekt ist <<null>>");
		}

		return umfeldDatenArt;
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse also alle
	 * Umfelddatenarten.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<UmfeldDatenArt> getInstanzen() {
		if (UmfeldDatenArt.typAufArt.isEmpty()) {
			throw new RuntimeException("Umfelddatenarten wurden noch nicht initialisiert");
		}

		return UmfeldDatenArt.typAufArt.values();
	}

	/**
	 * Initialisierung.
	 *
	 * @param dav
	 *            eine Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException
	 *             wenn nicht alle Objekte initialisiert werden konnten
	 */
	public static void initialisiere(final ClientDavInterface dav) throws DUAInitialisierungsException {
		if (!UmfeldDatenArt.typAufArt.isEmpty()) {
			Debug.getLogger().error("Umfelddatenarten duerfen nur einmal initialisiert werden");
		}
		final DataModel datenModell = dav.getDataModel();
		for (final UmfeldDatenArt art : UmfeldDatenArt.values()) {
			final SystemObjectType type = datenModell.getType(art.typPid);
			if (null != type) {
				art.init(type);
				UmfeldDatenArt.typAufArt.put(type, art);
			}
		}
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param typPid
	 *            die PID des Systemobjekttyps der Umfelddatenart
	 * @param abkuerzung
	 *            die Abkürzung der Umfelddatenart
	 * @param statusOffset
	 *            Offset für die Statuswerte fehlerhaft, nicht ermittelbar und
	 *            fehlerhaft/nicht ermittelbar.
	 **/
	UmfeldDatenArt(final String typPid, final String abkuerzung, long statusOffset) {

		this.typPid = typPid;
		this.abkuerzung = abkuerzung;
		this.statusOffset = statusOffset;
		name = typPid.substring("typ.ufds".length());

	}

	private void init(final SystemObjectType sysObjType) {
		this.type = sysObjType;
		final IntegerAttributeType attType = (IntegerAttributeType) sysObjType.getDataModel()
				.getAttributeType("att.ufds" + name);
		if (attType != null) {
			final IntegerValueRange range = attType.getRange();
			if (range != null) {
				skalierung = range.getConversionFactor();
			}
		}
	}

	/**
	 * Erfragt die Skalierung von Werten dieses Typs im Datenkatalog.
	 *
	 * @return die Skalierung von Werten dieses Typs im Datenkatalog
	 */
	public double getSkalierung() {
		return skalierung;
	}

	/**
	 * Erfragt den Nam.
	 *
	 * @return der Name der Umfelddatenart
	 */
	public String getName() {
		return name;
	}

	/**
	 * Erfragt die Abkürzung der Umfelddatenart.
	 *
	 * @return die Abkürzung der Umfelddatenart
	 */
	public String getAbkuerzung() {
		return abkuerzung;
	}

	/**
	 * Erfragt den Systemobjekt-Typ des Umfelddatensensors.
	 *
	 * @return den Systemobjekt-Typ des Umfelddatensensors
	 */
	public SystemObjectType getTyp() {
		return type;
	}

	@Override
	public String toString() {
		return name + " (" + abkuerzung + ")";
	}

	public long getStatusOffset() {
		return statusOffset;
	}

}
