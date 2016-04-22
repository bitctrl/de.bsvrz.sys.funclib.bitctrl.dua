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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Ableitungen dieser Klasse stellen die Standard-Publikationsinformationen für
 * <b>einen</b> Modul-Typ und <b>eine</b> SWE innerhalb der DUA zur Verfügung.
 * <br>
 * Diese Informationen können im Konstruktor dieser Klasse für je eine
 * SWE-Modultyp-Kombination als Instanz der Klasse
 * <code>StandardAspekteAdapter</code> angelegt werden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class StandardAspekteVersorger {

	/** Verbindung zum Verwaltungsmodul. */
	protected final IVerwaltung verwaltung;

	/**
	 * Die Informationen über die Standardaspekte für die Publikation einer
	 * bestimmten SWE-Modultyp-Kombinationen (hier mit leerem Objekt
	 * initialisiert, das zurückgegeben wird, wenn die Standardaspekte nicht zur
	 * Verfügung stehen).
	 */
	protected IStandardAspekte standardAspekte = new IStandardAspekte() {

		@Override
		public Collection<DAVObjektAnmeldung> getStandardAnmeldungen(final SystemObject[] objektFilter) {
			return new ArrayList<>();
		}

		@Override
		public Aspect getStandardAspekt(final ResultData originalDatum) {
			return null;
		}

		@Override
		public Collection<AttributeGroup> getAlleAttributGruppen() {
			return new ArrayList<>();
		}

	};

	/**
	 * Standardkonstruktor.
	 *
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @throws DUAInitialisierungsException
	 *             wird weitergereicht
	 */
	public StandardAspekteVersorger(final IVerwaltung verwaltung) throws DUAInitialisierungsException {
		this.verwaltung = verwaltung;
		init();
	}

	/**
	 * Initialisiert die Standard-Publikationsinformationen für einen kompletten
	 * Modul-Typ und eine SWE.
	 *
	 * @throws DUAInitialisierungsException
	 *             wenn es Probleme beim Initialisieren der
	 *             Standard-Publikationsinformationen gab
	 */
	protected abstract void init() throws DUAInitialisierungsException;

	/**
	 * Erfragt die Standardpublikations-Informationen.
	 *
	 * @return eine <code>IStandardAspekte</code>-Schnittstelle zu den
	 *         Standardpublikations-Informationen
	 */
	public final IStandardAspekte getStandardPubInfos() {
		return standardAspekte;
	}

	/**
	 * In diesen Objekten werden alle Informationen über das standardmäßige
	 * Publikationsverhalten von SWE-Modul-Typ- Kombinationen festgehalten.
	 * Diese Objekte werden innerhalb von <code>StandardAspekteVersorger</code>
	 * statisch erstellt und können über die statische Methode
	 * <code>getStandardPubInfos(..)</code> ausgelesen werden.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class StandardAspekteAdapter implements IStandardAspekte {

		/**
		 * Notwendige Datenanmeldungen.
		 */
		private final Collection<DAVObjektAnmeldung> anmeldungenGlobal = new TreeSet<>();

		/**
		 * Mapping von Systemobjekt-Attributgruppe-Aspekt-Kombination auf einen
		 * Standardpublikationsaspekt.
		 */
		private final Map<DAVObjektAnmeldung, Aspect> publikationsMap = new TreeMap<>();

		/**
		 * Standardkonstruktor.
		 *
		 * @param zuordnungen
		 *            Liste mit Standardpublikationszurodnungen
		 * @throws DUAInitialisierungsException
		 *             wenn Standard- Publikaionsinformationen nicht angelegt
		 *             werden konnten
		 */
		public StandardAspekteAdapter(final StandardPublikationsZuordnung[] zuordnungen)
				throws DUAInitialisierungsException {
			if (zuordnungen != null) {
				for (final StandardPublikationsZuordnung zuordnung : zuordnungen) {
					try {
						for (final SystemObject finObj : DUAUtensilien.getBasisInstanzen(zuordnung.typ,
								verwaltung.getVerbindung(), verwaltung.getKonfigurationsBereiche())) {
							anmeldungenGlobal.add(new DAVObjektAnmeldung(finObj,
									new DataDescription(zuordnung.atg, zuordnung.aspAusgang)));
						}

						final DataDescription originalDesc = new DataDescription(zuordnung.atg, zuordnung.aspEingang);
						for (final SystemObject obj : zuordnung.typ.getElements()) {
							if (obj.isValid()) {
								final DAVObjektAnmeldung objektAnmeldung = new DAVObjektAnmeldung(obj, originalDesc);
								publikationsMap.put(objektAnmeldung, zuordnung.aspAusgang);
							}
						}
					} catch (final Exception e) {
						throw new DUAInitialisierungsException(
								"Standard-" + "Publikaionsinformationen konnten nicht angelegt werden: " + e);
					}
				}
			}
		}

		@Override
		public Aspect getStandardAspekt(final ResultData originalDatum) {
			Aspect ergebnis = null;

			if (originalDatum != null) {
				try {
					final DAVObjektAnmeldung objektAnmeldung = new DAVObjektAnmeldung(originalDatum.getObject(),
							originalDatum.getDataDescription());

					ergebnis = publikationsMap.get(objektAnmeldung);
				} catch (final IllegalArgumentException e) {
					Debug.getLogger().fine(
							"Der Standard-Publikationsaspekt konnte" + "nicht ermittelt werden: " + originalDatum, e);
				}
			}

			return ergebnis;
		}

		@Override
		public final Collection<DAVObjektAnmeldung> getStandardAnmeldungen(final SystemObject[] objektFilter) {
			Collection<DAVObjektAnmeldung> anmeldungen = new TreeSet<>();

			if ((objektFilter == null) || (objektFilter.length <= 0)) {
				anmeldungen = anmeldungenGlobal;
			} else {
				final HashSet<SystemObject> objekte = new HashSet<>();
				for (final SystemObject obj : objektFilter) {
					objekte.add(obj);
				}

				for (final DAVObjektAnmeldung anmeldung : anmeldungenGlobal) {
					if (objekte.contains(anmeldung.getObjekt())) {
						anmeldungen.add(anmeldung);
					}
				}
			}

			return anmeldungen;
		}

		@Override
		public String toString() {
			String s = "Objekt-Anmeldungen:\n";

			for (final DAVObjektAnmeldung anmeldung : anmeldungenGlobal) {
				s += anmeldung;
			}

			return s;
		}

		@Override
		public Collection<AttributeGroup> getAlleAttributGruppen() {
			final Collection<AttributeGroup> attributGruppen = new TreeSet<>();

			for (final DAVObjektAnmeldung anmeldung : anmeldungenGlobal) {
				attributGruppen.add(anmeldung.getDatenBeschreibung().getAttributeGroup());
			}

			return attributGruppen;
		}

	}

	/**
	 * Zuordnung einer bestimmten
	 * <code>SystemObjectType-AttributeGroup-Aspect</code>-Kombination zu einem
	 * Standardpublikationsaspekt.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class StandardPublikationsZuordnung {

		/**
		 * Objekttyp eines Originaldatums.
		 */
		private SystemObjectType typ;

		/**
		 * Attributgruppe eines Originadatums.
		 */
		private AttributeGroup atg;

		/**
		 * Aspekt eines Originaldatums.
		 */
		private Aspect aspEingang;

		/**
		 * Standardpublikationsaspekt.
		 */
		private Aspect aspAusgang;

		/**
		 * Standardkonstruktor.
		 *
		 * @param typ
		 *            Objekttyp des Originaldatums
		 * @param atg
		 *            Attributgruppe des Originadatums
		 * @param aspEingang
		 *            Aspekt des Originaldatums
		 * @param aspAusgang
		 *            Standardpublikationsaspekt für die <code>SystemObjectType-
		 *            AttributeGroup-Aspect</code>-Kombination
		 * @throws DUAInitialisierungsException
		 *             falls eines der übergebenen DAV-Elemente nicht ausgelesen
		 *             werden konnte
		 */
		public StandardPublikationsZuordnung(final String typ, final String atg, final String aspEingang,
				final String aspAusgang) throws DUAInitialisierungsException {
			try {
				final DataModel dataModel = verwaltung.getVerbindung().getDataModel();
				this.typ = dataModel.getType(typ);
				if (null == this.typ) {
					throw new IllegalArgumentException("Typ '" + typ + "' ist nicht definiert");
				}
				this.atg = dataModel.getAttributeGroup(atg);
				if (null == this.atg) {
					throw new IllegalArgumentException("Atg '" + atg + "' ist nicht definiert");
				}
				this.aspEingang = dataModel.getAspect(aspEingang);
				this.aspAusgang = dataModel.getAspect(aspAusgang);
			} catch (final Exception ex) {
				throw new DUAInitialisierungsException(
						"Standardpublikationsaspekt" + " konnte" + " nicht eingerichtet werden ", ex);
			}
		}
	}

	protected IVerwaltung getVerwaltung() {
		return verwaltung;
	}

	protected void setStandardAspekte(final IStandardAspekte standardAspekte) {
		this.standardAspekte = standardAspekte;
	}
}
