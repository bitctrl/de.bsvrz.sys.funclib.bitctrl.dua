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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen;

import java.util.Collection;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;

/**
 * Dieses Interface stellt alle Informationen über die aktuelle
 * Datenflusssteuerung <b>für eine bestimmte SWE und einen bestimmten
 * Modul-Typ</b> zur Verfügung. Im Wesentlichen stellt es den Zugriff auf ein
 * Objekt des Typs <code>DatenFlussSteuerung</code> sicher.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public interface IDatenFlussSteuerungFuerModul {

	/**
	 * Erfragt die Menge aller Datenanmeldungen die in Bezug auf die übergebenen
	 * Objekte durchgeführt werden müssen, um diese nach der Plausibilisierung
	 * publizieren zu können.<br>
	 * <b>Achtung:</b> Wenn eine Überschneidung von Anmeldungen für
	 * Standard-Publikationsaspekte mit Anmeldungen aus der Datenflusssteuerung
	 * besteht, für die <b>nicht publizieren</b> gesetzt ist, so gilt hier auch
	 * die Standardpublikation als ausgeschaltet.
	 *
	 * @param filterObjekte
	 *            Liste mit (finalen) Objekten. Diese Liste gilt als Filter,
	 *            durch den alle innerhalb dieser Publikationszuordnung
	 *            definierten Datenanmeldungen geschickt werden, bevor diese
	 *            Methode ein Ergebnis zurückgibt. <code>null</code> = kein
	 *            Filter
	 * @param standardAnmeldungen
	 *            Menge der Anmeldungen unter den Standard-Publikationsaspekten
	 * @return eine ggf. leere Menge mit Datenanmeldungen
	 */
	Collection<DAVObjektAnmeldung> getDatenAnmeldungen(final SystemObject[] filterObjekte,
			final Collection<DAVObjektAnmeldung> standardAnmeldungen);

	/**
	 * Erfragt eine publikationsfähige Modifikation des übergebenen
	 * Original-Datums. Es wird ein Datum zurückgegeben, das nach der
	 * Plausibilisierung so publiziert werden muss.
	 *
	 * @param originalDatum
	 *            das Originaldatum, wie es vom plausibilisierenden Modul
	 *            empfangen wurde
	 * @param plausibilisiertesDatum
	 *            dessen <code>Data</code>-Objekt nach der Plausibilisierung
	 * @param standardAspekt
	 *            der Standardaspekt der Publikation für dieses Datum oder
	 *            <code>null</code>, wenn es keinen Standardaspekt gibt
	 * @return ein <code>ResultData</code>-Objekt, das so publiziert werden kann
	 *         oder <code>null</code>, wenn keine Publikation notwendig ist
	 *         (dies ist z.B. auch der Fall, wenn innerhalb der
	 *         Datenflusssteuerung der übergebene Standardaspekt explizit von
	 *         der Publikation ausgeschlossen wurde)
	 */
	ResultData getPublikationsDatum(final ResultData originalDatum, final Data plausibilisiertesDatum,
			final Aspect standardAspekt);

}
