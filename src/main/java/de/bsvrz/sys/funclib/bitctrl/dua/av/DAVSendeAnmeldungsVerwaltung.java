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

package de.bsvrz.sys.funclib.bitctrl.dua.av;

import java.util.Collection;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Verwaltungsklasse für Datenanmeldungen zum Senden von Daten. Über die Methode
 * <code>modifiziereDatenAnmeldung(..)</code> lassen sich Daten anmelden bzw.
 * abmelden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DAVSendeAnmeldungsVerwaltung extends DAVAnmeldungsVerwaltung implements ClientSenderInterface {

	/**
	 * Rolle des Senders.
	 */
	private final SenderRole rolle;

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteilerverbindung
	 * @param rolle
	 *            Rolle
	 */
	public DAVSendeAnmeldungsVerwaltung(final ClientDavInterface dav, final SenderRole rolle) {
		super(dav);
		this.rolle = rolle;
	}

	/**
	 * Sendet ein Datum in den Datenverteiler unter der Vorraussetzung, dass die
	 * Sendesteuerung für dieses Datum einen Empfänger bzw. eine Senke
	 * festgestellt hat
	 *
	 * @param resultat
	 *            ein zu sendendes Datum
	 */
	public final void sende(final ResultData resultat) {
		try {
			final DAVObjektAnmeldung anmeldung = new DAVObjektAnmeldung(resultat);
			final SendeStatus status = getAnmeldeStatus(anmeldung);

			if ((status == null) || (status.getStatus() == ClientSenderInterface.START_SENDING)) {
				final boolean imMomentKeineDaten;
				final boolean alsNaechstestKeineDaten = resultat.getData() == null;

				if (status != null) {
					imMomentKeineDaten = status.isImMomentKeineDaten();
					if (status.isImMomentKeineDaten() != alsNaechstestKeineDaten) {
						setAnmeldung(anmeldung,
								new SendeStatus(ClientSenderInterface.START_SENDING, alsNaechstestKeineDaten));
					}
				} else {
					imMomentKeineDaten = true;
					setAnmeldung(anmeldung,
							new SendeStatus(ClientSenderInterface.START_SENDING, alsNaechstestKeineDaten));
				}

				if ((alsNaechstestKeineDaten && !imMomentKeineDaten) || !alsNaechstestKeineDaten) {
					getDav().sendData(resultat);
				}
			}
		} catch (final DataNotSubscribedException e) {
			e.printStackTrace();
			Debug.getLogger().error(Constants.EMPTY_STRING, e);
		} catch (final SendSubscriptionNotConfirmed e) {
			e.printStackTrace();
			Debug.getLogger().error(Constants.EMPTY_STRING, e);
		}
	}

	@Override
	protected String abmelden(final Collection<DAVObjektAnmeldung> abmeldungen) {
		String info = Constants.EMPTY_STRING;
		if (DAVAnmeldungsVerwaltung.DEBUG) {
			info = "keine\n";
			if (abmeldungen.size() > 0) {
				info = "\n";
			}
		}
		for (final DAVObjektAnmeldung abmeldung : abmeldungen) {
			getDav().unsubscribeSender(this, abmeldung.getObjekt(), abmeldung.getDatenBeschreibung());
			removeAnmeldung(abmeldung);
			if (DAVAnmeldungsVerwaltung.DEBUG) {
				info += abmeldung;
			}
		}
		return info;
	}

	@Override
	protected String anmelden(final Collection<DAVObjektAnmeldung> anmeldungen) {
		String info = Constants.EMPTY_STRING;
		if (DAVAnmeldungsVerwaltung.DEBUG) {
			info = "keine\n";
			if (anmeldungen.size() > 0) {
				info = "\n";
			}
		}
		for (final DAVObjektAnmeldung anmeldung : anmeldungen) {
			try {
				getDav().subscribeSender(this, anmeldung.getObjekt(), anmeldung.getDatenBeschreibung(), rolle);
				setAnmeldung(anmeldung, null);
				if (DAVAnmeldungsVerwaltung.DEBUG) {
					info += anmeldung;
				}
			} catch (final OneSubscriptionPerSendData e) {
				Debug.getLogger().error("Probleme beim" + " Anmelden als Sender/Quelle:\n" + anmeldung, e);
				throw new RuntimeException(e);
			}
		}
		return info;
	}

	@Override
	public void dataRequest(final SystemObject object, final DataDescription dataDescription, final byte state) {
		final DAVObjektAnmeldung anmeldung = new DAVObjektAnmeldung(object, dataDescription);
		final SendeStatus status = getAnmeldeStatus(anmeldung);

		if ((status == null) || status.isImMomentKeineDaten()) {
			setAnmeldung(anmeldung, new SendeStatus(state, true));
		} else {
			setAnmeldung(anmeldung, new SendeStatus(state, false));
		}
	}

	@Override
	public boolean isRequestSupported(final SystemObject object, final DataDescription dataDescription) {
		final DAVObjektAnmeldung anmeldung = new DAVObjektAnmeldung(object, dataDescription);
		return isAngemeldet(anmeldung);
	}

	@Override
	protected String getInfo() {
		return rolle.toString();
	}
}
