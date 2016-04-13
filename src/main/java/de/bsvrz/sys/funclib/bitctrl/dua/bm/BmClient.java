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

package de.bsvrz.sys.funclib.bitctrl.dua.bm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Empfaengt Betriebsmeldungen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class BmClient implements ClientReceiverInterface {

	/**
	 * statische Instanz dieser Klasse.
	 */
	protected static BmClient instanz;

	/**
	 * Beobachter.
	 */
	private final Set<IBmListener> listeners = Collections.synchronizedSet(new HashSet<IBmListener>());

	/**
	 * Erfragt die statische Instanz dieser Klasse.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @return die statische Instanz dieser Klasse
	 */
	public static final BmClient getInstanz(final ClientDavInterface dav) {
		if (BmClient.instanz == null) {
			BmClient.instanz = new BmClient(dav);
		}
		return BmClient.instanz;
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 */
	protected BmClient(final ClientDavInterface dav) {
		final DataDescription datenBeschreibung = new DataDescription(
				dav.getDataModel().getAttributeGroup("atg.betriebsMeldung"),
				dav.getDataModel().getAspect("asp.information"));
		dav.subscribeReceiver(this, dav.getDataModel().getConfigurationAuthority(), datenBeschreibung,
				ReceiveOptions.normal(), ReceiverRole.receiver());
	}

	/**
	 * Fuegt dieser Klasse einen Listener hinzu.
	 *
	 * @param listener
	 *            ein neuer Listener
	 */
	public void addListener(final IBmListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * Loescht einen Listener.
	 *
	 * @param listener
	 *            ein alter Listener
	 */
	public void removeListener(final IBmListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void update(final ResultData[] results) {
		if (results != null) {
			for (final ResultData result : results) {
				if ((result != null) && (result.getData() != null)) {
					final long zeit = result.getDataTime();
					final String text = result.getData().getTextValue("MeldungsText").getText();
					SystemObject referenz = null;
					if ((result.getData().getReferenceArray("Referenz") != null)
							&& (result.getData().getReferenceArray("Referenz").getLength() > 0)
							&& (result.getData().getReferenceArray("Referenz").getReferenceValue(0) != null)) {
						referenz = result.getData().getReferenceArray("Referenz").getReferenceValue(0)
								.getSystemObject();
					}
					synchronized (listeners) {
						for (final IBmListener listener : listeners) {
							listener.aktualisiereBetriebsMeldungen(referenz, zeit, text);
						}
					}
				}
			}
		}
	}

}
