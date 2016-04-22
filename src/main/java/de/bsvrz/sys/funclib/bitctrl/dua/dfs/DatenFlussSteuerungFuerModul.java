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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;

/**
 * Diese Klasse stellt über die Schnittstelle
 * <code>IDatenFlussSteuerungFuerModul</code> alle Informationen über die
 * Datenflusssteuerung einer bestimmten SWE in Zusammenhang mit einem bestimmten
 * Modul-Typ zur Verfügung.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DatenFlussSteuerungFuerModul implements IDatenFlussSteuerungFuerModul {

	/**
	 * Liste aller Publikationszuordnungen innerhalb der Attributgruppe.
	 */
	private final Collection<PublikationsZuordung> publikationsZuordnungen = new ArrayList<>();

	/**
	 * Eine Map von einer Objekt-Attributgruppe-Kombination auf die Information
	 * ob, und unter welchem Aspekt publiziert werden soll.
	 */
	private final Map<PublikationObjAtg, PublikationFuerDatum> publikationsMap = new TreeMap<>();

	/**
	 * Fügt diesem Objekt eine Publikationszuordung hinzu.
	 *
	 * @param pz
	 *            die neue Publikationszuordung
	 */
	public final void add(final PublikationsZuordung pz) {
		for (final DAVObjektAnmeldung anmeldung : pz.getObjektAnmeldungen()) {
			final PublikationObjAtg pubObjAtg = new PublikationObjAtg(anmeldung.getObjekt(),
					anmeldung.getDatenBeschreibung().getAttributeGroup());
			final PublikationFuerDatum pub = new PublikationFuerDatum(pz.isPublizieren(), pz.getAspekt());

			publikationsMap.put(pubObjAtg, pub);
		}
		publikationsZuordnungen.add(pz);
	}

	@Override
	public Collection<DAVObjektAnmeldung> getDatenAnmeldungen(final SystemObject[] filterObjekte,
			final Collection<DAVObjektAnmeldung> standardAnmeldungen) {
		final Collection<DAVObjektAnmeldung> alleAnmeldungen = new TreeSet<>();
		final Collection<DAVObjektAnmeldung> stdAnmeldungen = new TreeSet<>();
		stdAnmeldungen.addAll(standardAnmeldungen);

		for (final PublikationsZuordung pz : publikationsZuordnungen) {
			if (pz.isPublizieren()) {
				final Collection<SystemObject> pzAnzumeldendeObjekte = new HashSet<>();

				if ((filterObjekte != null) && (filterObjekte.length > 0)) {
					for (final SystemObject obj : pz.getObjekte()) {
						for (final SystemObject filterObj : filterObjekte) {
							if (obj.equals(filterObj)) {
								pzAnzumeldendeObjekte.add(obj);
								break;
							}
						}
					}
				} else {
					pzAnzumeldendeObjekte.addAll(pz.getObjekte());
				}

				for (final DAVObjektAnmeldung pzAnmeldung : pz.getObjektAnmeldungen()) {
					if (pzAnzumeldendeObjekte.contains(pzAnmeldung.getObjekt())) {
						alleAnmeldungen.add(pzAnmeldung);
					}
				}
			} else {
				stdAnmeldungen.removeAll(pz.getObjektAnmeldungen());
			}
		}
		alleAnmeldungen.addAll(stdAnmeldungen);

		return alleAnmeldungen;
	}

	@Override
	public final ResultData getPublikationsDatum(final ResultData originalDatum, final Data plausibilisiertesDatum,
			final Aspect standardAspekt) {
		ResultData ergebnis = null;
		Aspect publikationsAspect = null;

		final PublikationObjAtg pubObjAtg = new PublikationObjAtg(originalDatum.getObject(),
				originalDatum.getDataDescription().getAttributeGroup());
		final PublikationFuerDatum pubDatum = publikationsMap.get(pubObjAtg);

		if (pubDatum != null) {
			if (pubDatum.publizieren) {
				publikationsAspect = pubDatum.asp;
			} else {
				if (pubDatum.asp != standardAspekt) {
					publikationsAspect = standardAspekt;
				}
			}
		} else {
			publikationsAspect = standardAspekt;
		}

		if (publikationsAspect != null) {
			final DataDescription dd = new DataDescription(originalDatum.getDataDescription().getAttributeGroup(),
					publikationsAspect);
			ergebnis = new ResultData(originalDatum.getObject(), dd, originalDatum.getDataTime(),
					plausibilisiertesDatum);
		}

		return ergebnis;
	}

	@Override
	public String toString() {
		String s = "\nDatenflusssteuerung für Modul:\n";

		int i = 0;
		for (final PublikationsZuordung pz : publikationsZuordnungen) {
			s += "Publikationszuordnung: " + (i++) + "\n" + pz;
		}

		return s;
	}

	/**
	 * Diese Klasse wird nur als Schlüssel-Objekt innerhalb der internen
	 * Struktur <code>publikationsMap</code> benötigt. Sie speichert ein
	 * (finales) Systemobjekt zusammen mit einer Attributgruppe. Die Klasse ist
	 * so designed, dass sie effektiv als Schlüssel innerhalb von
	 * <code>TreeMap</code>-Objekten eingesetzt werden kann.
	 */
	protected class PublikationObjAtg implements Comparable<PublikationObjAtg> {

		/**
		 * ein Systemobjekt.
		 */
		private final SystemObject obj;

		/**
		 * eine Attributgruppe.
		 */
		private final AttributeGroup atg;

		/**
		 * Standardkonstruktor.
		 *
		 * @param obj
		 *            ein Objekt
		 * @param atg
		 *            eine Attributgruppe
		 */
		protected PublikationObjAtg(final SystemObject obj, final AttributeGroup atg) {
			this.obj = obj;
			this.atg = atg;
		}

		@Override
		public int compareTo(final PublikationObjAtg that) {
			int result = Long.valueOf(obj.getId()).compareTo(that.obj.getId());

			if (result == 0) {
				result = Long.valueOf(atg.getId()).compareTo(that.atg.getId());
			}

			return result;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + getOuterType().hashCode();
			result = (prime * result) + ((atg == null) ? 0 : atg.hashCode());
			result = (prime * result) + ((obj == null) ? 0 : obj.hashCode());
			return result;
		}

		private DatenFlussSteuerungFuerModul getOuterType() {
			return DatenFlussSteuerungFuerModul.this;
		}

		/**
		 * Diese Methode muss implementiert werden, da nach der Exploration des
		 * Baums über <code>compareTo(..)</code> (bspw. beim Aufruf von
		 * <code>contains()</code>) nochmals mit <code>equals(..)</code>
		 * explizit auf Gleichheit getestet wird.
		 */
		@Override
		public boolean equals(final Object obj1) {
			boolean result = false;

			if (obj1 instanceof PublikationObjAtg) {
				final PublikationObjAtg that = (PublikationObjAtg) obj1;
				result = obj.equals(that.obj) && that.atg.equals(that.atg);
			}

			return result;
		}

	}

	/**
	 * Diese Klasse wird nur als Wert-Objekt zu einem Schlüssel vom Typ
	 * <code>PublikationObjAtg</code> innerhalb der internen Struktur
	 * <code>publikationsMap</code> benötigt. Sie speichert die Information, ob
	 * und unter welchem Aspekt publiziert werden soll.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class PublikationFuerDatum {

		/**
		 * Soll publiziert werden?
		 */
		private final boolean publizieren;

		/**
		 * Unter welchem Aspekt soll ein Datum publiziert werden.
		 */
		private final Aspect asp;

		/**
		 * Standardkonstruktor.
		 *
		 * @param publizieren
		 *            soll publiziert werden?
		 * @param asp
		 *            unter welchem Aspekt soll ein Datum publiziert werden
		 */
		protected PublikationFuerDatum(final boolean publizieren, final Aspect asp) {
			this.asp = asp;
			this.publizieren = publizieren;
		}
	}

}
