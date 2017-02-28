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

package de.bsvrz.sys.funclib.bitctrl.dua.lve.daten;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit der Attributgruppe
 * <code>atg.messQuerschnittVirtuellVLage</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class AtgMessQuerschnittVirtuellVLage implements MessQuerschnittAnteile {

	/**
	 * Liste der Messquerschnitte mit Angabe der Berechnungsvorschrift, wie aus
	 * diesen Messquerschnittswerten die Werte des virtuellen Messquerschnitts
	 * ermittelt werden sollen.
	 */
	private final AtlMessQuerSchnittBestandTeil[] messQuerSchnittBestandTeile;

	/**
	 * Messquerschnitt von dem die Geschwindigkeit uebernommen werden soll. Ist
	 * dieser nicht explizit versorgt wird standardmaessig der erste aus der
	 * Liste der Anteile genommen.
	 */
	private SystemObject messQuerschnittGeschwindigkeit;

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Verbindung zum Datenverteiler.
	 * @param objekt
	 *            der virtuelle Messquerschnitt, dessen Konfigurationsdaten
	 *            ausgelesen werden sollen.
	 * @throws KeineDatenException
	 *             Wird geworfen, wenn die konfigurierende Attributgruppe nicht
	 *             definiert ist.
	 *             @deprecated ClientDavInterface wird nicht benötigt
	 */
	@Deprecated
	public AtgMessQuerschnittVirtuellVLage(final ClientDavInterface dav, final SystemObject objekt)
			throws KeineDatenException {
		this(objekt);
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param objekt
	 *            der virtuelle Messquerschnitt, dessen Konfigurationsdaten
	 *            ausgelesen werden sollen.
	 * @throws KeineDatenException
	 *             Wird geworfen, wenn die konfigurierende Attributgruppe nicht
	 *             definiert ist.
	 */
	public AtgMessQuerschnittVirtuellVLage(final SystemObject objekt)
			throws KeineDatenException {
		final Data atgData = objekt
				.getConfigurationData(objekt.getDataModel().getAttributeGroup(DUAKonstanten.ATG_MQ_VIRTUELL_V_LAGE));
		if (atgData == null) {
			throw new KeineDatenException("Die Attributgruppe \"atg.messQuerschnittVirtuellVLage\" von VMQ " + objekt
					+ " ist nicht definiert.");
		}

		if (atgData.getReferenceValue("MessQuerschnittGeschwindigkeit") != null) {
			messQuerschnittGeschwindigkeit = atgData.getReferenceValue("MessQuerschnittGeschwindigkeit")
					.getSystemObject();
		}

		final List<AtlMessQuerSchnittBestandTeil> dummy = new ArrayList<>();
		for (int i = 0; i < atgData.getArray("MessQuerSchnittBestandTeile").getLength(); i++) {
			try {
				final AtlMessQuerSchnittBestandTeil anteil = new AtlMessQuerSchnittBestandTeil(
						atgData.getArray("MessQuerSchnittBestandTeile").getItem(i));
				dummy.add(anteil);
			} catch (final InvalidArgumentException e) {
				e.printStackTrace();
				Debug.getLogger().warning(
						"Problem beim Auslesen von Konfigurationsdaten von VMQ " + objekt + ":\n" + e.getMessage());
			}
		}

		messQuerSchnittBestandTeile = dummy.toArray(new AtlMessQuerSchnittBestandTeil[0]);
	}

	
	/**
	 * Erfragt Liste der Messquerschnitte mit Angabe der Berechnungsvorschrift,
	 * wie aus diesen Messquerschnittswerten die Werte des virtuellen
	 * Messquerschnitts ermittelt werden sollen.
	 *
	 * @return (ggf. leere) Liste der Messquerschnitte mit Angabe der
	 *         Berechnungsvorschrift.
	 */
	@Override
	public AtlMessQuerSchnittBestandTeil[] getMessQuerSchnittBestandTeile() {
		return messQuerSchnittBestandTeile;
	}

	/**
	 * Erfragt den Messquerschnitt von dem die Geschwindigkeit uebernommen
	 * werden soll. Ist dieser nicht explizit versorgt wird null zurückgegeben.
	 *
	 * @return der Messquerschnitt von dem die Geschwindigkeit uebernommen
	 *         werden soll.
	 */
	@Override
	public SystemObject getMessQuerschnittGeschwindigkeit() {
		return messQuerschnittGeschwindigkeit;
	}

	/**
	 * Korrespondiert mit der Attributliste
	 * <code>atl.messQuerSchnittBestandTeile</code>.
	 */
	public static final class AtlMessQuerSchnittBestandTeil {

		/**
		 * Referenz auf MQ.
		 */
		private final SystemObject mqReferenz;

		/**
		 * Berechnungsanteil des MQ am VMQ.
		 */
		private double anteil = Double.NaN;

		/**
		 * Standardkonstruktor.
		 *
		 * @param datum
		 *            das Datum mit MQ-Referenz und Anteil.
		 * @throws InvalidArgumentException
		 *             wenn die MQ-Referenz <code>null</code> ist.
		 */
		AtlMessQuerSchnittBestandTeil(final Data datum) throws InvalidArgumentException {
			if (datum.getReferenceValue("MessQuerschnittReferenz") == null) {
				throw new InvalidArgumentException(
						"Attribut \"MessQuerschnittReferenz\" konnte nicht ausgelesen werden");
			}
			if (datum.getReferenceValue("MessQuerschnittReferenz").getSystemObject() == null) {
				throw new InvalidArgumentException("Attribut \"MessQuerschnittReferenz\" ist <<null>>");
			}
			mqReferenz = datum.getReferenceValue("MessQuerschnittReferenz").getSystemObject();
			final long anteilDummy = datum.getUnscaledValue("Anteil").longValue();
			if (anteilDummy == 100) {
				anteil = 1.0;
			} else if (anteilDummy == -100) {
				anteil = -1.0;
			} else {
				anteil = datum.getScaledValue("Anteil").doubleValue();
			}

			if (anteil == 0) {
				throw new InvalidArgumentException(
						"Attribut \"Anteil\" ist 0. Dieser Datensatz kann vernachlaessigt werden.");
			}
		}

		/**
		 * Erfragt die Referenz auf MQ.
		 *
		 * @return Referenz auf MQ.
		 */
		public SystemObject getMQReferenz() {
			return mqReferenz;
		}

		/**
		 * Erfragt den Berechnungsanteil des MQ am VMQ.
		 *
		 * @return der Berechnungsanteil des MQ am VMQ.
		 */
		public double getAnteil() {
			return anteil;
		}

	}

}
