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

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messStelle</code>
 *
 * TODO Parameter auslesen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class MessStelle extends AbstractSystemObjekt {

	/**
	 * Zufahrten zu dieser Messstelle.
	 */
	private final Collection<MessQuerschnittAllgemein> zufahrten = new HashSet<>();

	/**
	 * Abfahrten von dieser Messstelle.
	 */
	private final Collection<MessQuerschnittAllgemein> abfahrten = new HashSet<>();

	/**
	 * Referenz auf den MessQuerschnitt, der zu prüfen ist.
	 */
	private SystemObject pruefling;

	private DuaVerkehrsNetz netz;

	/**
	 * Standardkontruktor.
	 *
	 * @param netz
	 *            das verwendete Verkehrsnetz
	 * @param msObjekt
	 *            ein Systemobjekt vom Typ <code>typ.messStelle</code>
	 * @throws DUAInitialisierungsException
	 *             wenn die Messstelle nicht initialisiert werden konnte
	 */
	protected MessStelle(DuaVerkehrsNetz netz, final SystemObject msObjekt) throws DUAInitialisierungsException {
		super(msObjekt);

		this.netz = netz;
		
		final ConfigurationObject konfigObjekt = (ConfigurationObject) msObjekt;
		final ObjectSet mqMengeAbfahrten = konfigObjekt.getNonMutableSet("Abfahrten");
		for (final SystemObject mqObj : mqMengeAbfahrten.getElements()) {
			if (mqObj.isValid()) {
				final MessQuerschnittAllgemein mqa = netz.getMessQuerSchnittAllgemein(mqObj);
				if (mqa != null) {
					abfahrten.add(mqa);
				} else {
					Debug.getLogger()
							.warning("Abfahrt " + mqObj + " an " + msObjekt + " konnte nicht identifiziert werden");
				}
			}
		}

		final ObjectSet mqMengeZufahrten = konfigObjekt.getNonMutableSet("Zufahrten");
		for (final SystemObject mqObj : mqMengeZufahrten.getElements()) {
			if (mqObj.isValid()) {
				final MessQuerschnittAllgemein mqa = netz.getMessQuerSchnittAllgemein(mqObj);
				if (mqa != null) {
					zufahrten.add(mqa);
				} else {
					Debug.getLogger()
							.warning("Zufahrt " + mqObj + " an " + msObjekt + " konnte nicht identifiziert werden");
				}
			}
		}

		final AttributeGroup atgEigenschaften = msObjekt.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_MESS_STELLE);
		final Data eigenschaften = msObjekt.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			Debug.getLogger()
					.warning("\"atg.messStelle\" von Messstelle " + msObjekt + " konnten nicht ausgelesen werden");
		} else {
			if (eigenschaften.getReferenceValue("Prüfling") != null) {
				pruefling = eigenschaften.getReferenceValue("Prüfling").getSystemObject();
			}
		}
	}

	static Map<SystemObject, MessStelle> einlesen(DuaVerkehrsNetz netz, final ClientDavInterface dav1, final ConfigurationArea[] kbs)
			throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		Map<SystemObject, MessStelle> result = new LinkedHashMap<>();
		for (final SystemObject msObj : dav1.getDataModel().getType(DUAKonstanten.TYP_MESS_STELLE).getElements()) {
			if (msObj.isValid() && DUAUtensilien.isObjektInKBsEnthalten(msObj, kbs)) {
				result.put(msObj, new MessStelle(netz, msObj));
			}
		}

		return result;
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param msObjekt
	 *            ein MessStellen-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 * @deprecated es sollte auf eine Instanz des Typ {@link DuaVerkehrsNetz}
	 *             zugegriffen werden
	 */
	@Deprecated
	public static MessStelle getInstanz(final SystemObject msObjekt) {

		DuaVerkehrsNetz verkehrsNetz = DuaVerkehrsNetz.getDefaultInstance();
		if (verkehrsNetz == null) {
			throw new RuntimeException("MessStellen-Klasse wurde noch nicht initialisiert");
		}
		return verkehrsNetz.getMessStelle(msObjekt);
	}

	/**
	 * Erfragt die Menge der Messquerschnitte der Zufahrten dieser Messstelle.
	 *
	 * @return ggf. leere Menge der Messquerschnitte der Zufahrten dieser
	 *         Messstelle
	 */
	public final Collection<MessQuerschnittAllgemein> getZufahrten() {
		return zufahrten;
	}

	/**
	 * Erfragt die Menge der Messquerschnitte der Abfahrten dieser Messstelle.
	 *
	 * @return ggf. leere Menge der Messquerschnitte der Abfahrten dieser
	 *         Messstelle
	 */
	public final Collection<MessQuerschnittAllgemein> getAbfahrten() {
		return abfahrten;
	}

	/**
	 * Erfragt Referenz auf den MessQuerschnitt, der zu prüfen ist.
	 *
	 * @return Referenz auf den MessQuerschnitt, der zu prüfen ist
	 */
	public final MessQuerschnittAllgemein getPruefling() {
		return netz.getMessQuerSchnittAllgemein(pruefling);
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessStelle.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}
}
