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

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.AtgMessQuerschnittVirtuell;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.AtgMessQuerschnittVirtuellVLage;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.KeineDatenException;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.MessQuerschnittAnteile;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.MessQuerschnittVirtuellLage;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnittVirtuell</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class MessQuerschnittVirtuell extends MessQuerschnittAllgemein {

	/**
	 * Indiziert die Vorschrift, nach der die virtuellen MQs berechnet werden
	 * sollen.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	public enum BerechnungsVorschrift {

		/**
		 * <b>DUMMY.</b><br>
		 * Steht fuer die Information, dass keine der beiden moeglichen
		 * Attributgruppen versorgt sind.
		 */
		UNBEKANNT,

		/**
		 * <b>NEUE VORSCHRIFT.</b><br>
		 * Definiert abstrakt eine Menge von (MQ, Anteil)-Tupeln, die
		 * beschreiben, wie sich die Werte des virtuellen MQ aus denen der
		 * einzelnen-MQs anteilig errechnen. Optionale Angabe eines MQ, von dem
		 * die Geschwindigkeit übernommen wird.
		 */
		AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE,

		/**
		 * <b>ALTE VORSCHRIFT.</b><br>
		 * Definiert konkret Einfahrten und Ausfahrten sowie die Streckenstuecke
		 * <code>vor</code>, <code>mitte</code> und <code>nach</code> und
		 * errechnet anhand dieser Informationen nach Regeln aus Afo 4.0 die
		 * Werte des virtuellen MQ.
		 */
		AUF_BASIS_VON_ATG_MQ_VIRTUELL_STANDARD
	}

	/**
	 * Die aktuelle Vorschrift, nach der die virtuellen MQs berechnet werden
	 * sollen.
	 */
	private BerechnungsVorschrift berechnungsVorschrift = BerechnungsVorschrift.UNBEKANNT;

	/**
	 * Messquerschnitt VOR der Anschlussstelle.
	 */
	private MessQuerschnitt mqVorObj;

	/**
	 * Messquerschnitt NACH der Anschlussstelle.
	 */
	private MessQuerschnitt mqNachObj;

	/**
	 * Messquerschnitt MITTE der Anschlussstelle.
	 */
	private MessQuerschnitt mqMitteObj;

	/**
	 * Messquerschnitt AUSFAHRT der Anschlussstelle.
	 */
	private MessQuerschnitt mqAusfahrtObj;

	/**
	 * Messquerschnitt EINFAHRT der Anschlussstelle.
	 */
	private MessQuerschnitt mqEinfahrtObj;

	/**
	 * die Lage des virtuellen MQs.
	 */
	private MessQuerschnittVirtuellLage mqVirtLage;

	/**
	 * die Bestandteile dieses VMQ.
	 */
	private MessQuerschnittAnteile messQuerschnittAnteile;

	private DuaVerkehrsNetz netz;

	/**
	 * Standardkontruktor.
	 *
	 * @param netz
	 *            das verwendete Verkehrsnetz
	 * @param mqvObjekt
	 *            ein Systemobjekt vom Typ
	 *            <code>typ.messQuerschnittVirtuell</code>
	 * @throws DUAInitialisierungsException
	 *             wenn der virtuelle Messquerschnitt nicht initialisiert werden
	 *             konnte
	 */
	private MessQuerschnittVirtuell(DuaVerkehrsNetz netz, final SystemObject mqvObjekt) throws DUAInitialisierungsException {
		super(mqvObjekt);

		this.netz = netz;
		
		if (mqvObjekt == null) {
			throw new NullPointerException("Uebergebenes MessQuerschnittVirtuell-Systemobjekt ist <<null>>");
		}

		final AttributeGroup atgEigenschaftenSTD = mqvObjekt.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_MQ_VIRTUELL_STANDARD);
		final Data eigenschaftenSTD = mqvObjekt.getConfigurationData(atgEigenschaftenSTD);

		if (eigenschaftenSTD == null) {
			Debug.getLogger().fine("\"atg.messQuerschnittVirtuellStandard\" von MessQuerschnittVirtuell-Objekt "
					+ mqvObjekt + " konnten nicht ausgelesen werden");
		} else {
			mqVirtLage = MessQuerschnittVirtuellLage.getZustand(eigenschaftenSTD.getUnscaledValue("Lage").intValue());
			if (eigenschaftenSTD.getReferenceValue("MessQuerschnittVor") != null) {
				mqVorObj = netz.getMessQuerSchnitt(eigenschaftenSTD.getReferenceValue("MessQuerschnittVor").getSystemObject());
			}
			if (eigenschaftenSTD.getReferenceValue("MessQuerschnittNach") != null) {
				mqNachObj = netz.getMessQuerSchnitt(eigenschaftenSTD.getReferenceValue("MessQuerschnittNach").getSystemObject());
			}
			if (eigenschaftenSTD.getReferenceValue("MessQuerschnittMitte") != null) {
				mqMitteObj = netz.getMessQuerSchnitt(eigenschaftenSTD.getReferenceValue("MessQuerschnittMitte").getSystemObject());
			}
			if (eigenschaftenSTD.getReferenceValue("MessQuerschnittAusfahrt") != null) {
				mqAusfahrtObj = netz.getMessQuerSchnitt(eigenschaftenSTD.getReferenceValue("MessQuerschnittAusfahrt").getSystemObject());
			}
			if (eigenschaftenSTD.getReferenceValue("MessQuerschnittEinfahrt") != null) {
				mqEinfahrtObj = netz.getMessQuerSchnitt(eigenschaftenSTD.getReferenceValue("MessQuerschnittEinfahrt").getSystemObject());
			}
			berechnungsVorschrift = BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_STANDARD;
		}

		try {
			messQuerschnittAnteile = new AtgMessQuerschnittVirtuell(mqvObjekt);
			berechnungsVorschrift = BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE;
		} catch (final KeineDatenException e) {
			Debug.getLogger().fine("\"atg.messQuerschnittVirtuellVLage\" von MessQuerschnittVirtuell-Objekt "
					+ mqvObjekt + " konnten nicht ausgelesen werden:\n" + e.getMessage());
		}

		try {
			messQuerschnittAnteile = new AtgMessQuerschnittVirtuellVLage(mqvObjekt);
			berechnungsVorschrift = BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE;
		} catch (final KeineDatenException e) {
			Debug.getLogger().fine("\"atg.messQuerschnittVirtuellVLage\" von MessQuerschnittVirtuell-Objekt "
					+ mqvObjekt + " konnten nicht ausgelesen werden:\n" + e.getMessage());
		}

	}

	static Map<SystemObject, MessQuerschnittVirtuell> einlesen(DuaVerkehrsNetz netz, final ClientDavInterface dav1,
			final ConfigurationArea[] kbs) throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		Map<SystemObject, MessQuerschnittVirtuell> result = new LinkedHashMap<>();
		for (final SystemObject mqvObjekt : dav1.getDataModel().getType(DUAKonstanten.TYP_MQ_VIRTUELL).getElements()) {
			if (mqvObjekt.isValid() && DUAUtensilien.isObjektInKBsEnthalten(mqvObjekt, kbs)) {
				result.put(mqvObjekt, new MessQuerschnittVirtuell(netz, mqvObjekt));
			}
		}

		return result;
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 * @deprecated es sollte auf eine Instanz einer {@link DuaVerkehrsNetz}
	 *             zurückgegriffen werden.
	 */
	@Deprecated
	public static Collection<MessQuerschnittVirtuell> getInstanzen() {

		DuaVerkehrsNetz verkehrsNetz = DuaVerkehrsNetz.getDefaultInstance();
		if (verkehrsNetz == null) {
			throw new RuntimeException("MessQuerschnittVirtuell-Klasse wurde noch nicht initialisiert");
		}
		return verkehrsNetz.getAlleMessQuerSchnitteVirtuell();
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param mqvObjekt
	 *            ein MessQuerschnittVirtuell-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 * @deprecated es sollte auf eine Instanz einer {@link DuaVerkehrsNetz}
	 *             zurückgegriffen werden.
	 */
	@Deprecated
	public static MessQuerschnittVirtuell getInstanz(final SystemObject mqvObjekt) {
		DuaVerkehrsNetz verkehrsNetz = DuaVerkehrsNetz.getDefaultInstance();
		if (verkehrsNetz == null) {
			throw new RuntimeException("MessQuerschnittVirtuell-Klasse wurde noch nicht initialisiert");
		}

		return verkehrsNetz.getMessQuerSchnittVirtuell(mqvObjekt);
	}

	@Override
	public Collection<FahrStreifen> getFahrStreifen() {
		final Collection<FahrStreifen> fahrStreifenMenge = new HashSet<>();

		if ((getBerechnungsVorschrift() == null)
				|| (getBerechnungsVorschrift() == BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE)) {
			for (final AtgMessQuerschnittVirtuellVLage.AtlMessQuerSchnittBestandTeil mqBestandteil : getMessQuerschnittAnteile()
					.getMessQuerSchnittBestandTeile()) {
				final MessQuerschnittAllgemein mqa = netz.getMessQuerSchnittAllgemein(mqBestandteil.getMQReferenz());
				if (mqa != null) {
					fahrStreifenMenge.addAll(mqa.getFahrStreifen());
				}
			}
		} else {
			if (getMQVor() != null) {
				fahrStreifenMenge.addAll(getMQVor().getFahrStreifen());
			}
			if (getMQNach() != null) {
				fahrStreifenMenge.addAll(getMQNach().getFahrStreifen());
			}
			if (getMQEinfahrt() != null) {
				fahrStreifenMenge.addAll(getMQEinfahrt().getFahrStreifen());
			}
			if (getMQAusfahrt() != null) {
				fahrStreifenMenge.addAll(getMQAusfahrt().getFahrStreifen());
			}
			if (getMQMitte() != null) {
				fahrStreifenMenge.addAll(getMQMitte().getFahrStreifen());
			}
		}

		return fahrStreifenMenge;
	}

	/**
	 * Erfragt Messquerschnitt VOR der Anschlussstelle.
	 *
	 * @return Messquerschnitt VOR der Anschlussstelle
	 */
	public final MessQuerschnitt getMQVor() {
		return mqVorObj;
	}

	/**
	 * Erfragt Messquerschnitt NACH der Anschlussstelle.
	 *
	 * @return Messquerschnitt NACH der Anschlussstelle
	 */
	public final MessQuerschnitt getMQNach() {
		return mqNachObj;
	}

	/**
	 * Erfragt Messquerschnitt MITTE der Anschlussstelle.
	 *
	 * @return Messquerschnitt MITTE der Anschlussstelle
	 */
	public final MessQuerschnitt getMQMitte() {
		return mqMitteObj;
	}

	/**
	 * Erfragt Messquerschnitt AUSFAHRT der Anschlussstelle.
	 *
	 * @return Messquerschnitt AUSFAHRT der Anschlussstelle
	 */
	public final MessQuerschnitt getMQAusfahrt() {
		return mqAusfahrtObj;
	}

	/**
	 * Erfragt Messquerschnitt EINFAHRT der Anschlussstelle.
	 *
	 * @return Messquerschnitt EINFAHRT der Anschlussstelle
	 */
	public final MessQuerschnitt getMQEinfahrt() {
		return mqEinfahrtObj;
	}

	/**
	 * Erfragt die Lage dieses virtuellen Messquerschnitts.
	 *
	 * @return die Lage dieses virtuellen Messquerschnitts.
	 */
	public final MessQuerschnittVirtuellLage getMQVirtuellLage() {
		return mqVirtLage;
	}

	/**
	 * Erfragt die aktuelle Vorschrift, nach der die virtuellen MQs berechnet
	 * werden sollen. Die Berechnungsvorschrift ergibt sich aus der Versorgung
	 * der beiden Attributgruppen:<br>
	 * - <code>atg.messQuerschnittVirtuellVLage</code> und<br>
	 * - <code>atg.messQuerschnittVirtuellStandard</code>.<br>
	 * Sind beide versorgt, so wird die Vorschrift auf Basis von
	 * <code>atg.messQuerschnittVirtuellVLage</code> bevorzugt.
	 *
	 * @return die aktuelle Vorschrift, nach der die virtuellen MQs berechnet
	 *         werden sollen.
	 */
	public final BerechnungsVorschrift getBerechnungsVorschrift() {
		return berechnungsVorschrift;
	}

	/**
	 * Erfragt die Konfigurationsinformationen aus der Attributgruppe
	 * <code>atg.messQuerschnittVirtuellVLage</code>.
	 *
	 * @deprecated ersetzt durch {@link #getMessQuerschnittAnteile()}
	 *
	 * @return die Konfigurationsinformationen aus der Attributgruppe
	 *         <code>atg.messQuerschnittVirtuellVLage</code>.
	 */
	@Deprecated
	public final AtgMessQuerschnittVirtuellVLage getAtgMessQuerschnittVirtuellVLage() {
		if (messQuerschnittAnteile instanceof AtgMessQuerschnittVirtuellVLage) {
			return (AtgMessQuerschnittVirtuellVLage) messQuerschnittAnteile;
		}
		return null;
	}

	/**
	 * Erfragt die Konfigurationsinformationen aus der Attributgruppe
	 * <code>atg.messQuerschnittVirtuellVLage</code>.
	 *
	 * @return die Konfigurationsinformationen aus der Attributgruppe
	 *         <code>atg.messQuerschnittVirtuellVLage</code>.
	 */
	public final MessQuerschnittAnteile getMessQuerschnittAnteile() {
		return messQuerschnittAnteile;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnittVirtuell.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}

}
