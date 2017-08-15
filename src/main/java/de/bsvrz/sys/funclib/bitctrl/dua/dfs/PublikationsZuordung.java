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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.ReferenceArray;
import de.bsvrz.dav.daf.main.Data.ReferenceValue;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * In dieser Klasse sind alle Informationen zusammengefasst, die das
 * Publikationsverhalten bezüglich <b>einer</b> bestimmten SWE, <b>einem</b>
 * bestimmten Modul-Typ und <b>einem</b> Publikationsaspekt beschreiben
 * innerhalb der Datenflusssteuerung beschreiben.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class PublikationsZuordung {

	/**
	 * Der Modul-Typ.
	 */
	private final ModulTyp modulTyp;

	/**
	 * Der Publikationsaspekt.
	 */
	private final Aspect aspekt;

	/**
	 * die (finalen) Objekte, für die ein Publikationsverhalten beschrieben ist.
	 */
	private final Collection<SystemObject> objekte = new HashSet<>();

	/**
	 * die Attributgruppen, für die ein Publikationsverhalten vorgesehen ist.
	 */
	private final Collection<AttributeGroup> atgs = new HashSet<>();

	/**
	 * soll publiziert werden.
	 */
	private final boolean publizieren;

	/**
	 * Die Objektanmeldungen, die innerhalb dieser Publikationszuordnung
	 * vorgesehen sind (bzw. bei <code>publizieren ==  false</code> explizit
	 * nicht vorgesehen)
	 */
	private final Collection<DAVObjektAnmeldung> anmeldungen = new TreeSet<>();

	/**
	 * Standardkonstruktor<br>
	 * <b>Achtung:</b> Sollte die Menge der übergebenen Objekte bzw.
	 * Attributgruppen leer sein, so werden <b>alle</b> Objekte bzw.
	 * Attributgruppen in den übergebenen Konfigurationskereichen (bzw. im
	 * Standardkonfigurationsbereich) angenommen.
	 *
	 * @param data
	 *            ein Datenverteiler-Datum mit den mit einer
	 *            Publikationszuordnung assoziierten Daten
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 */
	protected PublikationsZuordung(final Data data, final IVerwaltung verwaltung) {
		aspekt = (Aspect) data.getReferenceValue(DFSKonstanten.ATT_ASP).getSystemObject();
		modulTyp = ModulTyp.getZustand((int) data.getUnscaledValue(DFSKonstanten.ATT_MODUL_TYP).getState().getValue());
		publizieren = data.getTextValue(DFSKonstanten.ATT_PUBLIZIEREN).getText().toLowerCase().equals("ja");

		final ReferenceArray objArray = data.getReferenceArray(DFSKonstanten.ATT_OBJ);
		if (objArray.getLength() == 0) {
			objekte.addAll(DUAUtensilien.getBasisInstanzen(null, verwaltung.getVerbindung(),
					verwaltung.getKonfigurationsBereiche()));
		} else {
			for (final ReferenceValue refVal : objArray.getReferenceValues()) {
				objekte.addAll(DUAUtensilien.getBasisInstanzen(refVal.getSystemObject(), verwaltung.getVerbindung(),
						verwaltung.getKonfigurationsBereiche()));
			}
		}

		final ReferenceArray atgArray = data.getReferenceArray(DFSKonstanten.ATT_ATG);
		if (atgArray.getLength() == 0) {
			atgs.add(null);
		} else {
			for (final ReferenceValue refVal : atgArray.getReferenceValues()) {
				atgs.add((AttributeGroup) refVal.getSystemObject());
			}
		}

		for (final AttributeGroup atg : atgs) {
			final DataDescription datenBeschreibung = new DataDescription(atg, aspekt);

			for (final SystemObject finObj : objekte) {
				anmeldungen.addAll(
						DUAUtensilien.getAlleObjektAnmeldungen(finObj, datenBeschreibung, verwaltung.getVerbindung()));
			}
		}
	}

	/**
	 * Erfragt die Objektanmeldungen, die innerhalb dieser Publikationszuordnung
	 * vorgesehen sind (bzw. bei <code>publizieren ==  false</code> explizit
	 * nicht vorgesehen sind)
	 *
	 * @return eine Menge von Objektanmeldungen
	 */
	public Collection<DAVObjektAnmeldung> getObjektAnmeldungen() {
		return anmeldungen;
	}

	/**
	 * Erfragt den Aspekt.
	 *
	 * @return den Aspekt
	 */
	public Aspect getAspekt() {
		return aspekt;
	}

	/**
	 * Erfragt den Modul-Typ, für den diese Piblikationszuordnung gilt.
	 *
	 * @return der Modul-Typs
	 */
	public final ModulTyp getModulTyp() {
		return modulTyp;
	}

	/**
	 * Erfragt das Publikations-FLAG.
	 *
	 * @return das Publikations-FLAG
	 */
	public final boolean isPublizieren() {
		return publizieren;
	}

	/**
	 * Erfragt alle hier definierten Attributgruppen.
	 *
	 * @return alle hier definierten Attributgruppen
	 */
	public final Collection<AttributeGroup> getAtgs() {
		return atgs;
	}

	/**
	 * Erfragt die Menge aller hier definierten (finalen) Objekte.
	 *
	 * @return die Menge aller hier definierten (finalen) Objekte
	 */
	public final Collection<SystemObject> getObjekte() {
		return objekte;
	}

	/**
	 * Fragt, ob eine bestimmte Publikationszuordnung mit dieser hier kompatibel
	 * ist. Ob sie sich also widersprechen. Ein Widerspruch liegt vor, wenn:<br>
	 * 1. der Modul-Typ identisch ist UND<br>
	 * 2. für <b>beide</b> Objekte die Publikation eingeschalten ist UND<br>
	 * 3. der Publikationsaspekt der beiden Objekte <code>this</code> und
	 * <code>vergleichsObj</code> nicht identisch ist UND<br>
	 * 4. eine Objekt-Überschneidung innerhalb der Member-SystemObjekte von
	 * <code>this</code> und <code>vergleichsObj</code> besteht UND<br>
	 * 5. die Schnittmenge der Member-Attributgruppen nicht leer ist.<br>
	 *
	 * @param that
	 *            das Objekt, mit dem dieses verglichen werden soll
	 * @return <code>null</code> wenn kein Widerspruch vorliegt und eine den
	 *         Widerspruch illustrierende Fehlermeldung sonst.
	 */
	public final String isKompatibelMit(final PublikationsZuordung that) {
		if (modulTyp.equals(that.getModulTyp()) && // 1.
				isPublizieren() && that.isPublizieren() && // 2.
				!getAspekt().equals(that.getAspekt())) { // 3.

			for (final DAVObjektAnmeldung thisAnmeldung : getObjektAnmeldungen()) { // 4.
				// &
				// 5.
				for (final DAVObjektAnmeldung thatAnmeldung : that.getObjektAnmeldungen()) {
					if (thisAnmeldung.getObjekt().equals(thatAnmeldung.getObjekt())
							&& thisAnmeldung.getDatenBeschreibung().getAttributeGroup()
									.equals(thatAnmeldung.getDatenBeschreibung().getAttributeGroup())) {
						return "Die beiden Objektanmeldungen sind für" + " die Datenflusssteuerung widersprüchlich:\n"
								+ thisAnmeldung + "\n" + thatAnmeldung;
					}
				}
			}
		}

		return null; // keine Widersprüche
	}

	@Override
	public String toString() {
		String s = "Modul-Typ: " + modulTyp + "\n";
		s += "Aspekt: " + aspekt + "\n";
		s += "Publizieren: " + (publizieren ? "ja" : "nein") + "\n";
		for (final SystemObject obj : objekte) {
			s += "Objekt: " + obj + "\n";
		}
		for (final AttributeGroup atg : atgs) {
			s += "Atg: " + atg + "\n";
		}

		return s;
	}
}
