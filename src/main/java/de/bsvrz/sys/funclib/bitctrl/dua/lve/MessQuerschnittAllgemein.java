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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.MessQuerschnittTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnittAllgemein</code>
 * .
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class MessQuerschnittAllgemein extends AbstractSystemObjekt {

	/**
	 * Menge aller allgemeinen Messquerschnitte.
	 */
	private static Collection<MessQuerschnittAllgemein> mqaMenge;

	/**
	 * Systemobjekt des Ersatzmessquerschnitts für die Messwertersetzung.
	 */
	private SystemObject ersatzQuerschnittObj;

	/**
	 * Typ eines MessQuerschnitts (HauptFahrbahn, NebenFahrbahn, ...).
	 */
	private MessQuerschnittTyp typ;

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param mqaObjekt
	 *            Systemobjekt eines allgemeinen Messquerschnittes
	 */
	protected MessQuerschnittAllgemein(final ClientDavInterface dav, final SystemObject mqaObjekt) {
		super(mqaObjekt);

		if (mqaObjekt == null) {
			throw new NullPointerException("Übergebenes allgemeines Messquerschnittobjekt ist <<null>>");
		}

		final AttributeGroup atgEigenschaften = dav.getDataModel().getAttributeGroup(DUAKonstanten.ATG_MQ_ALLGEMEIN);
		final Data eigenschaften = mqaObjekt.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			Debug.getLogger().warning("\"atg.messQuerschnittAllgemein\" von allgemeinem Messquerschnittobjekt "
					+ mqaObjekt + " konnten nicht ausgelesen werden");
		} else {
			typ = MessQuerschnittTyp.getZustand(eigenschaften.getUnscaledValue("Typ").intValue());
			if (eigenschaften.getReferenceValue("ErsatzMessQuerschnitt") != null) {
				ersatzQuerschnittObj = eigenschaften.getReferenceValue("ErsatzMessQuerschnitt").getSystemObject();
			}
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessQuerschnittAllgemein> getAlleInstanzen() {
		if (MessQuerschnittAllgemein.mqaMenge == null) {
			MessQuerschnittAllgemein.mqaMenge = new HashSet<>();
			MessQuerschnittAllgemein.mqaMenge.addAll(MessQuerschnitt.getInstanzen());
			MessQuerschnittAllgemein.mqaMenge.addAll(MessQuerschnittVirtuell.getInstanzen());
		}

		return MessQuerschnittAllgemein.mqaMenge;
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param mqaObjekt
	 *            ein MessQuerschnittAllgemein-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 */
	public static MessQuerschnittAllgemein getInstanz(final SystemObject mqaObjekt) {
		MessQuerschnittAllgemein ergebnis = null;

		ergebnis = MessQuerschnitt.getInstanz(mqaObjekt);
		if (ergebnis == null) {
			ergebnis = MessQuerschnittVirtuell.getInstanz(mqaObjekt);
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge der mittelbar oder unmittelbar an diesem Querschnitt
	 * definierten Fahrstreifen.
	 *
	 * @return die Menge der mittelbar oder unmittelbar an diesem Querschnitt
	 *         definierten Fahrstreifen oder <code>null</code>, wenn hier keine
	 *         Fahrstreifen definiert sind
	 */
	public abstract Collection<FahrStreifen> getFahrStreifen();

	/**
	 * Erfragt den Ersatzquerschnitt dieses allgemeinen Messquerschnittes.
	 *
	 * @return den Ersatzquerschnitt dieses allgemeinen Messquerschnittes
	 */
	public final MessQuerschnittAllgemein getErsatzMessquerSchnitt() {
		return MessQuerschnittAllgemein.getInstanz(ersatzQuerschnittObj);
	}

	/**
	 * Erfragt den Typ dieses Messquerschnittes.
	 *
	 * @return der Typ dieses Messquerschnittes
	 */
	public final MessQuerschnittTyp getMQTyp() {
		return typ;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnittAllgemein.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}
}
