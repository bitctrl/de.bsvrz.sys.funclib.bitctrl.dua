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
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.FahrStreifenLage;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.fahrStreifen</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class FahrStreifen extends AbstractSystemObjekt {

	/**
	 * Mapt alle Fahrstreifen-Systemobjekte auf Objekte der Klasse
	 * <code>FahrStreifen</code>.
	 */
	protected static Map<SystemObject, FahrStreifen> sysObjFsObjMap = new HashMap<>();

	/**
	 * Datenverteiler-Verbindung.
	 */
	protected static ClientDavInterface sDav;

	/**
	 * die Lage dieses Fahrtreifens.
	 */
	private FahrStreifenLage lage;

	/**
	 * Systemobjekt des Ersatzfahrstreifens dieses Fahrstreifens.
	 */
	private SystemObject ersatzFahrstreifenObj;

	/**
	 * Systemobjekt des Nachbarfahrstreifens dieses Fahrstreifens.
	 */
	private SystemObject nachbarFahrstreifenObj;

	/**
	 * Standardkontruktor.
	 *
	 * @param fsObjekt
	 *            ein Systemobjekt vom Typ <code>typ.fahrStreifen</code>
	 * @throws DUAInitialisierungsException
	 *             wenn der Fahrstreifen nicht initialisiert werden konnte
	 */
	protected FahrStreifen(final SystemObject fsObjekt) throws DUAInitialisierungsException {
		super(fsObjekt);

		if (fsObjekt == null) {
			throw new NullPointerException("Übergebenes Fahrstreifenobjekt ist <<null>>");
		}

		final AttributeGroup atgEigenschaften = FahrStreifen.sDav.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_FAHRSTREIFEN);
		final Data eigenschaften = fsObjekt.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			Debug.getLogger().error(
					"\"atg.fahrStreifen\" von Fahrstreifenobjekt " + fsObjekt + " konnten nicht ausgelesen werden");
		} else {
			lage = FahrStreifenLage.getZustand(eigenschaften.getUnscaledValue("Lage").intValue());
			if (eigenschaften.getReferenceValue("ErsatzFahrStreifen") != null) {
				ersatzFahrstreifenObj = eigenschaften.getReferenceValue("ErsatzFahrStreifen").getSystemObject();
			}
		}
	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.fahrStreifen</code> statische Instanzen dieser Klasse angelegt
	 * werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException
	 *             wenn eines der Objekte nicht initialisiert werden konnte
	 */
	protected static final void initialisiere(final ClientDavInterface dav1) throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		if (FahrStreifen.sDav != null) {
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden");
		}
		FahrStreifen.sDav = dav1;

		for (final SystemObject fsObjekt : FahrStreifen.sDav.getDataModel().getType(DUAKonstanten.TYP_FAHRSTREIFEN)
				.getElements()) {
			if (fsObjekt.isValid()) {
				FahrStreifen.sysObjFsObjMap.put(fsObjekt, new FahrStreifen(fsObjekt));
			}
		}
	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.fahrStreifen</code> statische Instanzen dieser Klasse angelegt
	 * werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 * @param kbs
	 *            Menge der zu betrachtenden Konfigurationsbereiche
	 * @throws DUAInitialisierungsException
	 *             wenn eines der Objekte nicht initialisiert werden konnte
	 */
	protected static final void initialisiere(final ClientDavInterface dav1, final ConfigurationArea[] kbs)
			throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		if (FahrStreifen.sDav != null) {
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden");
		}
		FahrStreifen.sDav = dav1;

		for (final SystemObject fsObjekt : FahrStreifen.sDav.getDataModel().getType(DUAKonstanten.TYP_FAHRSTREIFEN)
				.getElements()) {
			if (fsObjekt.isValid() && DUAUtensilien.isObjektInKBsEnthalten(fsObjekt, kbs)) {
				FahrStreifen.sysObjFsObjMap.put(fsObjekt, new FahrStreifen(fsObjekt));
			}
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<FahrStreifen> getInstanzen() {
		if (FahrStreifen.sDav == null) {
			throw new RuntimeException("FahrStreifen-Klasse wurde noch nicht initialisiert");
		}
		return FahrStreifen.sysObjFsObjMap.values();
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param fsObjekt
	 *            ein Fahrstreifen-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 */
	public static final FahrStreifen getInstanz(final SystemObject fsObjekt) {
		if (FahrStreifen.sDav == null) {
			throw new RuntimeException("Fahrstreifen-Klasse wurde noch nicht initialisiert");
		}
		FahrStreifen ergebnis = null;

		if (fsObjekt != null) {
			ergebnis = FahrStreifen.sysObjFsObjMap.get(fsObjekt);
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Lage dieses Fahrtreifens innerhalb eines Messquerschnitts.
	 *
	 * @return die Lage dieses Fahrtreifens innerhalb eines Messquerschnitts
	 */
	public FahrStreifenLage getLage() {
		return lage;
	}

	/**
	 * Erfragt den Ersatzfahrstreifen dieses Fahrstreifens.
	 *
	 * @return den Ersatzfahrstreifen dieses Fahrstreifens oder
	 *         <code>null</code>, wenn dieser nicht ermittelt werden konnte
	 */
	public final FahrStreifen getErsatzFahrStreifen() {
		return FahrStreifen.getInstanz(ersatzFahrstreifenObj);
	}

	/**
	 * Setzt den Ersatzfahrstreifen dieses Fahrstreifens.
	 *
	 * @param ersatzFahrstreifenObj1
	 *            den Ersatzfahrstreifen dieses Fahrstreifens
	 */
	protected final void setErsatzFahrStreifen(final SystemObject ersatzFahrstreifenObj1) {
		ersatzFahrstreifenObj = ersatzFahrstreifenObj1;
	}

	/**
	 * Erfragt den Nachbarfahrstreifen dieses Fahrstreifens.
	 *
	 * @return den Nachbarfahrstreifen dieses Fahrstreifens oder
	 *         <code>null</code>, wenn dieser Fahrstreifen keinen
	 *         Nachbarfahrstreifen hat
	 */
	public final FahrStreifen getNachbarFahrStreifen() {
		return FahrStreifen.getInstanz(nachbarFahrstreifenObj);
	}

	/**
	 * Setzt den Nachbarfahrstreifen dieses Fahrstreifens.
	 *
	 * @param nachbarFahrstreifenObj1
	 *            den Nachbarfahrstreifen dieses Fahrstreifens
	 */
	protected final void setNachbarFahrStreifen(final SystemObject nachbarFahrstreifenObj1) {
		nachbarFahrstreifenObj = nachbarFahrstreifenObj1;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return FahrStreifen.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}

}
