/*
 * Allgemeine Funktionen für das Segment DuA
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
 * Copyright 2016 by Kappich Systemberatung Aachen
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

import java.text.SimpleDateFormat;

/**
 * Allgemeine Konstanten der DUA.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class DUAKonstanten {

	/**
	 * Standardkonstruktor.
	 */
	private DUAKonstanten() {

	}

	/**
	 * <code>null</code>-String.
	 */
	public static final String NULL = "<<null>>";

	/**
	 * Kommandozeilenargument: KonfigurationsBereichsPid.
	 */
	public static final String ARG_KONFIGURATIONS_BEREICHS_PID = "KonfigurationsBereichsPid";

	/**
	 * DAV-Typ-PID <code>typ.umfeldDatenSensor</code>.
	 */
	public static final String TYP_UFD_SENSOR = "typ.umfeldDatenSensor";

	/**
	 * DAV-Typ-PID <code>typ.umfeldDatenMessStelle</code>.
	 */
	public static final String TYP_UFD_MESSSTELLE = "typ.umfeldDatenMessStelle";

	/**
	 * DAV-Typ-PID <code>typ.messQuerschnittAllgemein</code>.
	 */
	public static final String TYP_MQ_ALLGEMEIN = "typ.messQuerschnittAllgemein";

	/**
	 * DAV-Typ-PID <code>typ.messQuerschnittVirtuell</code>.
	 */
	public static final String TYP_MQ_VIRTUELL = "typ.messQuerschnittVirtuell";

	/**
	 * DAV-Typ-PID <code>typ.messQuerschnitt</code>.
	 */
	public static final String TYP_MQ = "typ.messQuerschnitt";

	/**
	 * DAV-Typ-PID <code>typ.messStelle</code>.
	 */
	public static final String TYP_MESS_STELLE = "typ.messStelle";

	/**
	 * DAV-Typ-PID <code>typ.messStellenGruppe</code>.
	 */
	public static final String TYP_MESS_STELLEN_GRUPPE = "typ.messStellenGruppe";

	/**
	 * DAV-Typ-PID <code>typ.fahrStreifen</code>.
	 */
	public static final String TYP_FAHRSTREIFEN = "typ.fahrStreifen";

	/**
	 * DAV-Typ-PID <code>typ.fahrStreifenLangZeit</code>.
	 */
	public static final String TYP_FAHRSTREIFEN_LZ = "typ.fahrStreifenLangZeit";

	/**
	 * DAV-Typ-PID <code>typ.straßenabschnitt</code>.
	 */
	public static final String TYP_STRASSEN_ABSCHNITT = "typ.straßenAbschnitt";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitIntervall</code>.
	 */
	public static final String ATG_KZD = "atg.verkehrsDatenKurzZeitIntervall";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenLangZeitIntervall</code>.
	 */
	public static final String ATG_LZD = "atg.verkehrsDatenLangZeitIntervall";

	/**
	 * DAV-Atg-PID <code>"atg.störfallZustand"</code>.
	 */
	public static final String ATG_STOERFALL_ZUSTAND = "atg.störfallZustand";

	/**
	 * DAV-Asp-PID <code>asp.externeErfassung</code>.
	 */
	public static final String ASP_EXTERNE_ERFASSUNG = "asp.externeErfassung";

	/**
	 * DAV-Asp-PID <code>asp.plausibilitätsPrüfungFormal</code>.
	 */
	public static final String ASP_PL_PRUEFUNG_FORMAL = "asp.plausibilitätsPrüfungFormal";

	/**
	 * DAV-Asp-PID <code>asp.plausibilitätsPrüfungLogisch</code>.
	 */
	public static final String ASP_PL_PRUEFUNG_LOGISCH = "asp.plausibilitätsPrüfungLogisch";

	/**
	 * DAV-Asp-PID <code>asp.messWertErsetzung</code>.
	 */
	public static final String ASP_MESSWERTERSETZUNG = "asp.messWertErsetzung";

	/**
	 * DAV-Asp-PID <code>asp.analyse</code>.
	 */
	public static final String ASP_ANALYSE = "asp.analyse";

	/**
	 * DAV-Atg-PID <code>atg.fahrStreifen</code>.
	 */
	public static final String ATG_FAHRSTREIFEN = "atg.fahrStreifen";

	/**
	 * DAV-Atg-PID {@value} .
	 */
	public static final String ATG_UMFELD_DATEN_SENSOR = "atg.umfeldDatenSensor";

	/**
	 * DAV-Atg-PID <code>atg.messStelle</code>.
	 */
	public static final String ATG_MESS_STELLE = "atg.messStelle";

	/**
	 * DAV-Atg-PID <code>atg.messStellenGruppe</code>.
	 */
	public static final String ATG_MESS_STELLEN_GRUPPE = "atg.messStellenGruppe";

	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittAllgemein</code>.
	 */
	public static final String ATG_MQ_ALLGEMEIN = "atg.messQuerschnittAllgemein";

	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittVirtuell</code>.
	 */
	public static final String ATG_MQ_VIRTUELL = "atg.messQuerschnittVirtuell";

	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittVirtuellVLage</code>.
	 */
	public static final String ATG_MQ_VIRTUELL_V_LAGE = "atg.messQuerschnittVirtuellVLage";

	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittVirtuellStandard</code>.
	 */
	public static final String ATG_MQ_VIRTUELL_STANDARD = "atg.messQuerschnittVirtuellStandard";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitTrendExtraPolationFs</code>.
	 */
	public static final String ATG_KURZZEIT_TRENT_FS = "atg.verkehrsDatenKurzZeitTrendExtraPolationFs";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitTrendExtraPolationMq</code>.
	 */
	public static final String ATG_KURZZEIT_TRENT_MQ = "atg.verkehrsDatenKurzZeitTrendExtraPolationMq";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitGeglättetFs</code>.
	 */
	public static final String ATG_KURZZEIT_GEGLAETTET_FS = "atg.verkehrsDatenKurzZeitGeglättetFs";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitGeglättetMq</code>.
	 */
	public static final String ATG_KURZZEIT_GEGLAETTET_MQ = "atg.verkehrsDatenKurzZeitGeglättetMq";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitFs</code>.
	 */
	public static final String ATG_KURZZEIT_FS = "atg.verkehrsDatenKurzZeitFs";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitMq</code>.
	 */
	public static final String ATG_KURZZEIT_MQ = "atg.verkehrsDatenKurzZeitMq";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenDtvFs</code>.
	 */
	public static final String ATG_DTV_FS = "atg.verkehrsDatenDtvFs";

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenDtvMq</code>.
	 */
	public static final String ATG_DTV_MQ = "atg.verkehrsDatenDtvMq";

	/**
	 * Wert <b>Ja</b> von Attribut-Typ <code>att.jaNein</code>.
	 */
	public static final int JA = 1;

	/**
	 * Wert <b>Nein</b> von Attribut-Typ <code>att.jaNein</code>.
	 */
	public static final int NEIN = 0;

	/**
	 * Daten sind nicht ermittelbar (ist KEIN Fehler). Wird gesetzt, wenn der
	 * entsprechende Wert nicht ermittelbar ist und kein Interpolation sinnvoll
	 * möglich ist (z.B. ist die Geschwindigkeit nicht ermittelbar, wenn kein
	 * Fahrzeug erfasst wurde).
	 */
	public static final int NICHT_ERMITTELBAR = -1;

	/**
	 * Daten sind fehlerhaft. Wird gesetzt, wenn die Daten als fehlerhaft
	 * erkannt wurden.
	 */
	public static final int FEHLERHAFT = -2;

	/**
	 * Daten nicht ermittelbar, da bereits Basiswerte fehlerhaft. Wird gesetzt,
	 * wenn Daten, die zur Berechnung dieses Werts notwendig sind, bereits als
	 * fehlerhaft gekennzeichnet sind, oder wenn die Berechnung aus anderen
	 * Gründen (z.B. Nenner = 0 in der Berechnungsformel) nicht möglich war.
	 */
	public static final int NICHT_ERMITTELBAR_BZW_FEHLERHAFT = -3;

	/**
	 * Undefinierter (nicht initialisierter Messwert).
	 */
	public static final int MESSWERT_UNBEKANNT = -4;

	/**
	 * Mittelwertbildung <code>unbekannt</code>.
	 */
	public static final int MWB_UNBEKANNT = -1;

	/**
	 * Mittelwertbildung <code>gleitende Mittelwertbildung</code>.
	 */
	public static final int MWB_GLEITEND = 0;

	/**
	 * Mittelwertbildung <code>arithmetische Mittelwertbildung</code>.
	 */
	public static final int MWB_ARITHMETISCH = 1;

	/**
	 * Standard-Format der Zeitangabe innerhalb der Betriebsmeldungen.
	 */
	public static final SimpleDateFormat BM_ZEIT_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	/** FormatString der Zeitangabe mit Datum. */
	public static final String ZEIT_FORMAT_GENAU_STR = "dd.MM.yyyy HH:mm:ss.SSS";

	/**
	 * Genaues Format der Zeitangabe mit Datum.
	 *
	 * @deprecated Stringkonstante {@link #ZEIT_FORMAT_GENAU_STR} verwenden,
	 *             Instanz nicht threadsafe
	 */
	@Deprecated
	public static final SimpleDateFormat ZEIT_FORMAT_GENAU = new SimpleDateFormat(DUAKonstanten.ZEIT_FORMAT_GENAU_STR);

	/** Genaues Format der Zeitangabe ohne Datum. */
	public static final String NUR_ZEIT_FORMAT_GENAU_STR = "HH:mm:ss.SSS";

	/**
	 * Genaues Format der Zeitangabe ohne Datum.
	 *
	 * @deprecated Stringkonstante {@link #NUR_ZEIT_FORMAT_GENAU_STR} verwenden,
	 *             Instanz nicht threadsafe
	 */
	@Deprecated
	public static final SimpleDateFormat NUR_ZEIT_FORMAT_GENAU = new SimpleDateFormat(
			DUAKonstanten.NUR_ZEIT_FORMAT_GENAU_STR);

	/**
	 * Feld mit allen innerhalb eines KZD-Satzes beschriebenen Attributen.
	 */
	public static final String[] KZD_ATTRIBUTE = new String[] { "qKfz", "vKfz", "qLkw", "vLkw", "qPkw", "vPkw", "b",
			"tNetto", "sKfz", "vgKfz" };

	/**
	 * Aspekt <code>asp.tlsAntwort</code> für Antworten von TLS-Daten eines
	 * DE-Blocks nach Abruf, nach Pufferabfrage oder spontan.
	 */
	public static final String ASP_TLS_ANTWORT = "asp.tlsAntwort";

}
