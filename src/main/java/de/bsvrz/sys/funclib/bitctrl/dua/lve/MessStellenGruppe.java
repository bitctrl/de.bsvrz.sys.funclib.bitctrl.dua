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
import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messStellenGruppe</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class MessStellenGruppe extends AbstractSystemObjekt {

	/**
	 * Messstellen dieser Gruppe (sortiert wie in Konfiguration).
	 */
	private MessStelle[] messStellen = new MessStelle[0];

	/**
	 * Legt fest, ob die Ermittlung systematischer Detektorfehler für diese
	 * MessStellenGruppe durchgeführt werden soll.
	 */
	private boolean systematischeDetektorfehler;

	/**
	 * Standardkontruktor .
	 *
	 * @param netz
	 *            das verwendete Verkehrsnetz
	 * @param msgObjekt
	 *            ein Systemobjekt vom Typ <code>typ.messStellenGruppe</code>
	 * @throws DUAInitialisierungsException
	 *             wenn die Messstellengruppe nicht initialisiert werden konnte
	 */
	@SuppressWarnings("unused")
	protected MessStellenGruppe(DuaVerkehrsNetz netz, final SystemObject msgObjekt)
			throws DUAInitialisierungsException {
		super(msgObjekt);

		final ConfigurationObject konfigObjekt = (ConfigurationObject) msgObjekt;

		final AttributeGroup atgEigenschaften = msgObjekt.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_MESS_STELLEN_GRUPPE);
		final Data eigenschaften = msgObjekt.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			Debug.getLogger().warning("\"atg.messStellenGruppe\" von Messstellengruppe " + msgObjekt
					+ " konnten nicht ausgelesen werden");
		} else {
			if (eigenschaften.getReferenceArray("MessStellen") != null) {
				final MessStelle[] dummy = new MessStelle[eigenschaften.getReferenceArray("MessStellen").getLength()];
				int c = 0;
				for (int i = 0; i < eigenschaften.getReferenceArray("MessStellen").getLength(); i++) {
					if ((eigenschaften.getReferenceArray("MessStellen").getReferenceValue(i) != null) && (eigenschaften
							.getReferenceArray("MessStellen").getReferenceValue(i).getSystemObject() != null)) {
						dummy[c++] = netz.getMessStelle(
								eigenschaften.getReferenceArray("MessStellen").getReferenceValue(i).getSystemObject());
					}
				}

				if (c > 0) {
					messStellen = new MessStelle[c];
					for (int i = 0; i < c; i++) {
						messStellen[i] = dummy[i];
					}
				}
			}

			systematischeDetektorfehler = eigenschaften.getUnscaledValue("SystematischeDetektorfehler")
					.intValue() == DUAKonstanten.JA;
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 * @deprecated die verwendeten MessStellenGruppen sollten aus
	 *             {@link DuaVerkehrsNetz} ermittelt werden.
	 */
	@Deprecated
	public static Collection<MessStellenGruppe> getInstanzen() {

		DuaVerkehrsNetz verkehrsNetz = DuaVerkehrsNetz.getDefaultInstance();
		if (verkehrsNetz == null) {
			throw new RuntimeException("DuaVerkehrsNetz wurde noch nicht initialisiert");
		}
		return verkehrsNetz.getAlleMessStellenGruppen();
	}

	static Map<SystemObject, MessStellenGruppe> einlesen(DuaVerkehrsNetz netz, final ClientDavInterface dav1, final ConfigurationArea[] kbs)
			throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>");
		}

		Map<SystemObject, MessStellenGruppe> result = new LinkedHashMap<>();
		for (final SystemObject msObj : dav1.getDataModel().getType(DUAKonstanten.TYP_MESS_STELLEN_GRUPPE)
				.getElements()) {
			if (msObj.isValid() && DUAUtensilien.isObjektInKBsEnthalten(msObj, kbs)) {
				result.put(msObj, new MessStellenGruppe(netz, msObj));
			}
		}
		return result;
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param msgObjekt
	 *            ein MessStellenGruppe-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 * @deprecated die verwendeten MessStellenGruppen sollten aus
	 *             {@link DuaVerkehrsNetz} ermittelt werden.
	 */
	@Deprecated
	public static MessStellenGruppe getInstanz(final SystemObject msgObjekt) {
		DuaVerkehrsNetz verkehrsNetz = DuaVerkehrsNetz.getDefaultInstance();
		if (verkehrsNetz == null) {
			throw new RuntimeException("DuaVerkehrsNetz wurde noch nicht initialisiert");
		}
		return verkehrsNetz.getMessStellenGruppe(msgObjekt);
	}

	/**
	 * Erfragt die Menge der Messstellen dieser Messstellengruppe.
	 *
	 * @return ggf. leere Menge der Messstellen dieser Messstellengruppe
	 *         (sortiert wie in Konfiguration)
	 */
	public final MessStelle[] getMessStellen() {
		return messStellen;
	}

	/**
	 * Erfragt, ob die Ermittlung systematischer Detektorfehler für diese
	 * MessStellenGruppe durchgeführt werden soll.
	 *
	 * @return die Ermittlung systematischer Detektorfehler für diese
	 *         MessStellenGruppe durchgeführt werden soll
	 */
	public final boolean isSystematischeDetektorfehler() {
		return systematischeDetektorfehler;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessStellenGruppe.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}

}
