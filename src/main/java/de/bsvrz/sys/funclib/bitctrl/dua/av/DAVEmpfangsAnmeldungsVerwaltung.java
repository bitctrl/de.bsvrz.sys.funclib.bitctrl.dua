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
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;

/**
 * Verwaltungsklasse für Datenanmeldungen zum Empfangen von Daten. Über die
 * Methode <code>modifiziereDatenAnmeldung(..)</code> lassen sich Daten anmelden
 * bzw. abmelden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DAVEmpfangsAnmeldungsVerwaltung extends DAVAnmeldungsVerwaltung {

	/**
	 * Rolle des Empfängers.
	 */
	private final ReceiverRole rolle;

	/**
	 * Optionen.
	 */
	private final ReceiveOptions optionen;

	/**
	 * der Empfänger der Daten.
	 */
	private final ClientReceiverInterface empfaenger;

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteilerverbindung
	 * @param rolle
	 *            Rolle
	 * @param optionen
	 *            Optionen
	 * @param empfaenger
	 *            die Empfänger-Klasse der Datenverteiler- Daten, für die diese
	 *            Anmeldungs-Verwaltung arbeiten soll
	 */
	public DAVEmpfangsAnmeldungsVerwaltung(final ClientDavInterface dav, final ReceiverRole rolle,
			final ReceiveOptions optionen, final ClientReceiverInterface empfaenger) {
		super(dav);
		this.rolle = rolle;
		this.optionen = optionen;
		this.empfaenger = empfaenger;
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
			getDav().unsubscribeReceiver(empfaenger, abmeldung.getObjekt(), abmeldung.getDatenBeschreibung());
			removeAnmeldung(abmeldung);
			if (DAVAnmeldungsVerwaltung.DEBUG) {
				info += abmeldung;
			}
		}
		if (DAVAnmeldungsVerwaltung.DEBUG) {
			info += "von [" + empfaenger + "]\n";
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
			getDav().subscribeReceiver(empfaenger, anmeldung.getObjekt(), anmeldung.getDatenBeschreibung(), optionen, rolle);
			setAnmeldung(anmeldung, null);
			if (DAVAnmeldungsVerwaltung.DEBUG) {
				info += anmeldung;
			}
		}
		if (DAVAnmeldungsVerwaltung.DEBUG) {
			info += "fuer [" + empfaenger + "]\n";
		}
		return info;
	}


	@Override
	protected String getInfo() {
		return rolle + ", " + optionen;
	}
}
