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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.FahrStreifenLage;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnitt</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class MessQuerschnitt extends MessQuerschnittAllgemein {

	/**
	 * Mapt alle Messquerschnitt-Systemobjekte auf Objekte dieser Klasse.
	 */
	private static Map<SystemObject, MessQuerschnitt> sysObjMqObjMap = new HashMap<>();

	/**
	 * Datenverteiler-Verbindung.
	 */
	private static ClientDavInterface sDav;

	/**
	 * Menge der an diesem Messquerschnitt definierten Fahstreifen.
	 */
	private final List<FahrStreifen> fahrStreifen = new ArrayList<>();

	/**
	 * Standardkontruktor.
	 *
	 * @param mqObjekt
	 *            ein Systemobjekt vom Typ <code>typ.messQuerschnitt</code>
	 */
	protected MessQuerschnitt(final SystemObject mqObjekt) {
		super(MessQuerschnitt.sDav, mqObjekt);

		if (mqObjekt == null) {
			throw new NullPointerException("Übergebenes Messquerschnitt-Systemobjekt ist <<null>>");
		}

		final ConfigurationObject konfigObjekt = (ConfigurationObject) mqObjekt;
		final ObjectSet fsMenge = konfigObjekt.getNonMutableSet("FahrStreifen");
		for (final SystemObject fsObj : fsMenge.getElements()) {
			if (fsObj.isValid()) {
				final FahrStreifen fs = FahrStreifen.getInstanz(fsObj);
				if (fs != null) {
					fahrStreifen.add(fs);
				} else {
					Debug.getLogger().warning(
							"Fahrstreifen " + fsObj + " an " + mqObjekt + " konnte nicht identifiziert werden");
				}
			}
		}
	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.messQuerschnitt</code> statische Instanzen dieser Klasse
	 * angelegt werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 */
	protected static void initialisiere(final ClientDavInterface dav1) {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		if (MessQuerschnitt.sDav != null) {
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden");
		}
		MessQuerschnitt.sDav = dav1;

		for (final SystemObject mqObjekt : MessQuerschnitt.sDav.getDataModel().getType(DUAKonstanten.TYP_MQ)
				.getElements()) {
			if (mqObjekt.isValid()) {
				MessQuerschnitt.sysObjMqObjMap.put(mqObjekt, new MessQuerschnitt(mqObjekt));
			}
		}
	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.messQuerschnitt</code> statische Instanzen dieser Klasse
	 * angelegt werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 * @param kbs
	 *            Menge der zu betrachtenden Konfigurationsbereiche
	 */
	protected static void initialisiere(final ClientDavInterface dav1, final ConfigurationArea[] kbs) {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		if (MessQuerschnitt.sDav != null) {
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden");
		}
		MessQuerschnitt.sDav = dav1;

		for (final SystemObject mqObjekt : MessQuerschnitt.sDav.getDataModel().getType(DUAKonstanten.TYP_MQ)
				.getElements()) {
			if (mqObjekt.isValid() && DUAUtensilien.isObjektInKBsEnthalten(mqObjekt, kbs)) {
				MessQuerschnitt.sysObjMqObjMap.put(mqObjekt, new MessQuerschnitt(mqObjekt));
			}
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessQuerschnitt> getInstanzen() {
		if (MessQuerschnitt.sDav == null) {
			throw new RuntimeException("Messquerschnitt-Klasse wurde noch nicht initialisiert");
		}
		return MessQuerschnitt.sysObjMqObjMap.values();
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param mqObjekt
	 *            ein Messquerschnitt-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 */
	public static MessQuerschnitt getInstanz(final SystemObject mqObjekt) {
		if (MessQuerschnitt.sDav == null) {
			throw new RuntimeException("Messquerschnitt-Klasse wurde noch nicht initialisiert");
		}
		MessQuerschnitt ergebnis = null;

		if (mqObjekt != null) {
			ergebnis = MessQuerschnitt.sysObjMqObjMap.get(mqObjekt);
		}

		return ergebnis;
	}

	@Override
	public final List<FahrStreifen> getFahrStreifen() {
		return fahrStreifen;
	}

	/**
	 * Erfragt den Nachbarfahrstreifen des übergebenen Fahrstreifens<br>
	 * Der Nachbarfahrstreifen ist immer der in Fahrtrichtung linke Fahrstreifen
	 * bzw. der rechte, wenn es links keinen Fahrstreifen gibt.
	 *
	 * @param fs
	 *            ein Fahrstreifen
	 * @return der Nachbarfahrstreifen des übergebenen Fahrstreifens oder
	 *         <code>null</code> wenn dieser Fahrstreifen keinen
	 *         Nachbarfahrstreifen hat
	 */
	protected final FahrStreifen getNachbarVon(final FahrStreifen fs) {
		FahrStreifen nachbar = null;

		if (fs.getLage() != null) {
			final FahrStreifenLage lageLinksVonHier = fs.getLage().getLinksVonHier();
			final FahrStreifenLage lageRechtsVonHier = fs.getLage().getRechtsVonHier();

			FahrStreifen linkerNachbar = null;
			FahrStreifen rechterNachbar = null;

			for (final FahrStreifen fs1 : fahrStreifen) {
				if (lageLinksVonHier != null) {
					if (fs1.getLage().equals(lageLinksVonHier)) {
						linkerNachbar = fs1;
						break;
					}
				}
				if (lageRechtsVonHier != null) {
					if (fs1.getLage().equals(lageRechtsVonHier)) {
						rechterNachbar = fs1;
					}
				}
			}

			if (linkerNachbar != null) {
				nachbar = linkerNachbar;
			} else {
				nachbar = rechterNachbar;
			}
		}

		return nachbar;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnitt.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}
}
