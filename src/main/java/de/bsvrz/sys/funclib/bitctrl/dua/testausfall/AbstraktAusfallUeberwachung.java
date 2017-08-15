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

package de.bsvrz.sys.funclib.bitctrl.dua.testausfall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.KontrollProzess;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IKontrollProzessListener;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Abstrakte Ausfallüberwachung für zyklisch gesendete Daten.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class AbstraktAusfallUeberwachung extends AbstraktBearbeitungsKnotenAdapter
implements IKontrollProzessListener<Long> {

	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Mapt alle betrachteten Systemobjekte auf den aktuell für sie erlaubten
	 * maximalen Zeitverzug.
	 */
	private final Map<SystemObject, Long> objektWertErfassungVerzug = Collections
			.synchronizedMap(new TreeMap<SystemObject, Long>());

	/**
	 * Eine Map mit allen aktuellen Kontrollzeitpunkten und den zu diesen
	 * Kontrollzeitpunkten zu überprüfenden Systemobjekten.
	 */
	private final SortedMap<Long, Collection<ObjektResultat>> kontrollZeitpunkte = Collections
			.synchronizedSortedMap(new TreeMap<Long, Collection<ObjektResultat>>());

	/**
	 * interner Kontrollprozess.
	 */
	private KontrollProzess<Long> kontrollProzess;

	/**
	 * speichert pro Systemobjekt die letzte empfangene Datenzeit.
	 */
	private final Map<SystemObject, Long> letzteEmpfangeneDatenZeitProObj = new HashMap<>();

	/**
	 * Erfragt die Intervalllänge T eines Datums.
	 *
	 * @param resultat
	 *            ein Datum
	 * @return die im übergebenen Datum enthaltene Intervalllänge T
	 */
	protected abstract long getTVon(ResultData resultat);

	/**
	 * Erfragt das ausgefallene Datum, dass sich aus dem übergebenen Datum
	 * ergibt.
	 *
	 * @param originalResultat
	 *            ein Datum
	 * @return das ausgefallene Datum, dass sich aus dem übergebenen Datum
	 *         ergibt
	 */
	protected abstract ResultData getAusfallDatumVon(ResultData originalResultat);

	@Override
	public void initialisiere(final IVerwaltung dieVerwaltung) throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		kontrollProzess = new KontrollProzess<>();
		kontrollProzess.addListener(this);

		for (final SystemObject objekt : getVerwaltung().getSystemObjekte()) {
			letzteEmpfangeneDatenZeitProObj.put(objekt, new Long(-1));
		}
	}

	protected void setObjectWertErfassungVerzug(final SystemObject object, final Long zeitVerzug) {
		objektWertErfassungVerzug.put(object, zeitVerzug);
	}


	@Override
	public synchronized void aktualisiereDaten(final ResultData[] resultate) {
		if (resultate != null) {
			final List<ResultData> weiterzuleitendeResultate = new ArrayList<>();

			for (final ResultData resultat : resultate) {
				if (resultat != null) {

					if (getMaxZeitVerzug(resultat.getObject()) < 0) {
						/**
						 * Datum wird nicht ueberwacht
						 */
						weiterzuleitendeResultate.add(resultat);
					} else {
						/**
						 * Hier werden die Daten herausgefiltert, die von der
						 * Ausfallkontrolle quasi zu unrecht generiert wurden,
						 * da das Datum nur minimal zu spät kam.
						 */
						if (letzteEmpfangeneDatenZeitProObj.get(resultat.getObject()) < resultat.getDataTime()) {

							/**
							 * Zeitstempel ist echt neu!
							 */
							weiterzuleitendeResultate.add(resultat);
						}
						letzteEmpfangeneDatenZeitProObj.put(resultat.getObject(), resultat.getDataTime());

						if (resultat.getData() != null) {

							bereinigeKontrollZeitpunkte(resultat);

							final long kontrollZeitpunkt = getKontrollZeitpunktVon(resultat);
							if (kontrollZeitpunkt > 0) {
								Collection<ObjektResultat> kontrollObjekte = kontrollZeitpunkte.get(kontrollZeitpunkt);

								/**
								 * Kontrolldatum bestimmten
								 */
								final ObjektResultat neuesKontrollObjekt = new ObjektResultat(resultat);
								if (kontrollObjekte != null) {
									kontrollObjekte.add(neuesKontrollObjekt);
								} else {
									kontrollObjekte = new TreeSet<>();
									kontrollObjekte.add(neuesKontrollObjekt);
									kontrollZeitpunkte.put(new Long(kontrollZeitpunkt), kontrollObjekte);
								}
							}

							long fruehesterKontrollZeitpunkt = -1;

							if (!kontrollZeitpunkte.isEmpty()) {
								fruehesterKontrollZeitpunkt = kontrollZeitpunkte.firstKey().longValue();

								if (fruehesterKontrollZeitpunkt > 0) {
									kontrollProzess.setNaechstenAufrufZeitpunkt(fruehesterKontrollZeitpunkt,
											new Long(fruehesterKontrollZeitpunkt));
								} else {
									AbstraktAusfallUeberwachung.LOGGER.warning("Der momentan aktuellste Kontrollzeitpunkt ist <= 0");
								}
							} else {
								AbstraktAusfallUeberwachung.LOGGER.warning("Die Menge der Kontrollzeitpunkte ist leer");
							}
						}
					}
				}
			}

			if ((getKnoten() != null) && !weiterzuleitendeResultate.isEmpty()) {
				getKnoten().aktualisiereDaten(weiterzuleitendeResultate.toArray(new ResultData[0]));
			}
		}
	}

	/**
	 * Erfragt den maximalen Zeitverzug für ein Systemobjekt.
	 *
	 * @param obj
	 *            ein Systemobjekt
	 * @return der maximale Zeitverzug für das Systemobjekt oder -1, wenn dieser
	 *         nicht ermittelt werden konnte
	 */
	protected long getMaxZeitVerzug(final SystemObject obj) {
		long maxZeitVerzug = -1;

		if (obj != null) {
			synchronized (objektWertErfassungVerzug) {
				final Long dummy = objektWertErfassungVerzug.get(obj);
				if ((dummy != null) && (dummy > 0)) {
					maxZeitVerzug = dummy;
				}
			}
		}

		return maxZeitVerzug;
	}

	/**
	 * Bereinigt die Liste der Kontrollzeitpunkte anhand des gerade
	 * eingetroffenen Datums. Dabei wird zunächst der momentan noch erwartete
	 * Kontrollzeitpunkt dieses Datums berechnet und dieser dann aus der Liste
	 * der Kontrollzeitpunkte entfernt
	 *
	 * @param resultat
	 *            ein Roh-Datum eines Systemobjekts
	 */
	private synchronized void bereinigeKontrollZeitpunkte(final ResultData resultat) {

		/**
		 * Berechne den wahrscheinlichsten Zeitpunkt, für den hier noch auf ein
		 * Datum dieses Objektes gewartet wird
		 */
		final Long letzterErwarteterZeitpunkt = resultat.getDataTime() + getTVon(resultat)
		+ getMaxZeitVerzug(resultat.getObject());

		Collection<ObjektResultat> kontrollObjekte = kontrollZeitpunkte.get(letzterErwarteterZeitpunkt);

		final ObjektResultat datum = new ObjektResultat(resultat);

		/**
		 * Gibt es einen Kontrollzeitpunkt, für den das Objekt, des empfangenen
		 * Datums eingeplant sein müsste
		 */
		if (kontrollObjekte != null) {
			if (kontrollObjekte.remove(datum)) {
				if (kontrollObjekte.isEmpty()) {
					kontrollZeitpunkte.remove(letzterErwarteterZeitpunkt);
				}
			} else {
				kontrollObjekte = null;
			}
		}

		if (kontrollObjekte == null) {
			Long gefundenInKontrollZeitpunkt = new Long(-1);
			for (final Long kontrollZeitpunkt : kontrollZeitpunkte.keySet()) {
				kontrollObjekte = kontrollZeitpunkte.get(kontrollZeitpunkt);
				if (kontrollObjekte.remove(datum)) {
					gefundenInKontrollZeitpunkt = kontrollZeitpunkt;
					break;
				}
			}

			if ((kontrollObjekte != null) && (gefundenInKontrollZeitpunkt >= 0)) {
				if (kontrollObjekte.isEmpty()) {
					kontrollZeitpunkte.remove(gefundenInKontrollZeitpunkt);
				}
			} else {
				AbstraktAusfallUeberwachung.LOGGER.info("Datum " + datum + " konnte nicht aus" + " Kontrollwarteschlange gelöscht werden");
			}
		}
	}

	@Override
	public synchronized void trigger(final Long kontrollZeitpunkt) {
		final List<ResultData> zuSendendeAusfallDatenMenge = new ArrayList<>();

		final Collection<ObjektResultat> ausfallDaten = kontrollZeitpunkte.get(kontrollZeitpunkt);
		if (ausfallDaten != null) {
			for (final ObjektResultat ausfallDatum : ausfallDaten) {
				zuSendendeAusfallDatenMenge.add(getAusfallDatumVon(ausfallDatum.getDatum()));
			}
		} else {
			AbstraktAusfallUeberwachung.LOGGER.warning(
					"Der Kontrollzeitpunkt " + new SimpleDateFormat(DUAKonstanten.ZEIT_FORMAT_GENAU_STR).format(new Date(kontrollZeitpunkt))
					+ " wurde inzwischen entfernt");
		}

		/**
		 * Sende die ausgefallenen Daten an mich selbst, um die Liste der
		 * Ausfallzeitpunkte zu aktualisieren
		 */
		if (zuSendendeAusfallDatenMenge.size() > 0) {
			aktualisiereDaten(zuSendendeAusfallDatenMenge.toArray(new ResultData[0]));
		}
	}

	/**
	 * Erfragt den Zeitpunkt, zu dem von dem Objekt, das mit diesem Datensatz
	 * assoziiert ist, ein neuer Datensatz (spätestens) erwartet wird.
	 *
	 * @param empfangenesResultat
	 *            ein empfangener Datensatz
	 * @return der späteste Zeitpunkt des nächsten Datensatzes oder -1, wenn
	 *         dieser nicht sinnvoll bestimmt werden konnte (wenn z.B. keine
	 *         Parameter vorliegen)
	 */
	private long getKontrollZeitpunktVon(final ResultData empfangenesResultat) {
		long kontrollZeitpunkt = -1;

		final long maxZeitVerzug = getMaxZeitVerzug(empfangenesResultat.getObject());

		if (maxZeitVerzug >= 0) {
			kontrollZeitpunkt = empfangenesResultat.getDataTime() + getTVon(empfangenesResultat) + maxZeitVerzug;
		} else {
			AbstraktAusfallUeberwachung.LOGGER.fine("Es wurden noch keine (sinnvollen) Parameter empfangen: " + empfangenesResultat.getObject());
		}

		return kontrollZeitpunkt;
	}

	@Override
	public ModulTyp getModulTyp() {
		return null;
	}

	@Override
	public void aktualisierePublikation(final IDatenFlussSteuerung dfs) {
		// hier wird nicht publiziert
	}
}
