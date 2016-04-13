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
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Abstrakte Verwaltungsklasse für Datenanmeldungen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class DAVAnmeldungsVerwaltung {

	/**
	 * produziert ausfuehrlichere Log-Meldungen.
	 */
	protected static final boolean DEBUG = false;

	/**
	 * Baum der Datenanmeldungen, die im Moment aktuell sind (ggf. mit ihrem
	 * Status der Sendesteuerung).
	 */
	private final Map<DAVObjektAnmeldung, SendeStatus> aktuelleObjektAnmeldungen = new TreeMap<>();

	/**
	 * Datenverteilerverbindung.
	 */
	private final ClientDavInterface dav;

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteilerverbindung
	 */
	protected DAVAnmeldungsVerwaltung(final ClientDavInterface dav) {
		this.dav = dav;
	}

	/**
	 * Modifiziert die hier verwalteten Objektanmeldungen dergestalt, dass nur
	 * die innerhalb der übergebenen Liste beschriebenen Anmeldungen bestehen
	 * bleiben.<br>
	 * D.h. insbesondere, dass eine übergebene leere Liste alle bereits
	 * durchgeführten Anmeldungen wieder rückgängig macht.
	 *
	 * @param neueObjektAnmeldungen
	 *            die neue Liste mit Objektanmeldungen
	 */
	public final void modifiziereObjektAnmeldung(final Collection<DAVObjektAnmeldung> neueObjektAnmeldungen) {

		// Debug Anfang
		String info = Constants.EMPTY_STRING;
		if (DAVAnmeldungsVerwaltung.DEBUG) {
			info = "Verlangte Anmeldungen (" + getInfo() + "): ";
			if (neueObjektAnmeldungen.size() == 0) {
				info += "keine\n";
			} else {
				info += "\n";
			}
			for (final DAVObjektAnmeldung neueObjektAnmeldung : neueObjektAnmeldungen) {
				info += neueObjektAnmeldung;
			}
			info += "Bisherige Anmeldungen (" + getInfo() + "): ";
			if (aktuelleObjektAnmeldungen.size() == 0) {
				info += "keine\n";
			} else {
				info += "\n";
			}
			for (final DAVObjektAnmeldung aktuelleObjektAnmeldung : aktuelleObjektAnmeldungen.keySet()) {
				info += aktuelleObjektAnmeldung;
			}
		}
		// Debug Ende

		synchronized (this) {
			final Collection<DAVObjektAnmeldung> diffObjekteAnmeldungen = new TreeSet<>();
			for (final DAVObjektAnmeldung neueAnmeldung : neueObjektAnmeldungen) {
				if (!aktuelleObjektAnmeldungen.containsKey(neueAnmeldung)) {
					diffObjekteAnmeldungen.add(neueAnmeldung);
				}
			}

			final Collection<DAVObjektAnmeldung> diffObjekteAbmeldungen = new TreeSet<>();
			for (final DAVObjektAnmeldung aktuelleAnmeldung : aktuelleObjektAnmeldungen.keySet()) {
				if (!neueObjektAnmeldungen.contains(aktuelleAnmeldung)) {
					diffObjekteAbmeldungen.add(aktuelleAnmeldung);
				}
			}

			if (DAVAnmeldungsVerwaltung.DEBUG) {
				info += "--------\nABmeldungen: ";
				info += abmelden(diffObjekteAbmeldungen);
				info += "ANmeldungen: ";
				info += anmelden(diffObjekteAnmeldungen);
				Debug.getLogger().config(info);
			} else {
				abmelden(diffObjekteAbmeldungen);
				anmelden(diffObjekteAnmeldungen);
			}
		}
	}

	protected void removeAnmeldung(final DAVObjektAnmeldung abmeldung) {
		synchronized (aktuelleObjektAnmeldungen) {
			aktuelleObjektAnmeldungen.remove(abmeldung);
		}
	}

	protected void setAnmeldung(final DAVObjektAnmeldung anmeldung, final SendeStatus object) {
		synchronized (aktuelleObjektAnmeldungen) {
			aktuelleObjektAnmeldungen.put(anmeldung, object);
		}
	}

	protected SendeStatus getAnmeldeStatus(final DAVObjektAnmeldung anmeldung) {
		synchronized (aktuelleObjektAnmeldungen) {
			return aktuelleObjektAnmeldungen.get(anmeldung);
		}
	}

	protected boolean isAngemeldet(final DAVObjektAnmeldung anmeldung) {
		synchronized (aktuelleObjektAnmeldungen) {
			return aktuelleObjektAnmeldungen.containsKey(anmeldung);
		}
	}


	/**
	 * Führt alle übergebenen Daten<b>ab</b>meldungen durch.
	 *
	 * @param abmeldungen
	 *            durchzuführende Daten<b>ab</b>meldungen
	 * @return eine Liste aller <b>ab</b>gemeldeten Einzel-Anmeldungen als
	 *         Zeichenkette
	 */
	protected abstract String abmelden(final Collection<DAVObjektAnmeldung> abmeldungen);

	/**
	 * Führt alle übergebenen Daten<b>an</b>meldungen durch.
	 *
	 * @param anmeldungen
	 *            durchzuführende Daten<b>an</b>meldungen
	 * @return eine Liste aller neu <b>an</b>gemeldeten Einzel-Anmeldungen als
	 *         Zeichenkette
	 */
	protected abstract String anmelden(final Collection<DAVObjektAnmeldung> anmeldungen);

	/**
	 * Erfragt Informationen zum Anmeldungsverhalten.
	 *
	 * @return Informationen zum Anmeldungsverhalten
	 */
	protected abstract String getInfo();

	/**
	 * Der Zustand einer Datenbeschreibung bwzüglich der Sendesteuerung und der
	 * aktuell veroeffentlichten Daten.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class SendeStatus {

		/**
		 * aktueller Zustand der Sendesteuerung.
		 */
		private byte status = ClientSenderInterface.STOP_SENDING;

		/**
		 * indiziert, ob die Datenbeschreibung im Moment auf
		 * <code>keine Daten</code> oder <code>keine Quelle</code> steht.
		 */
		private boolean imMomentKeineDaten = true;

		/**
		 * Standardkonstruktor.
		 */
		public SendeStatus() {
		}

		/**
		 * Standardkonstruktor.
		 *
		 * @param status
		 *            aktueller Zustand der Sendesteuerung
		 * @param imMomentKeineDaten
		 *            indiziert, ob die Datenbeschreibung im Moment auf
		 *            <code>keine Daten</code> oder <code>keine Quelle</code>
		 *            steht
		 */
		public SendeStatus(final byte status, final boolean imMomentKeineDaten) {
			this.status = status;
			this.imMomentKeineDaten = imMomentKeineDaten;
		}

		/**
		 * Erfragt den aktuellen Zustand der Sendesteuerung.
		 *
		 * @return aktueller Zustand der Sendesteuerung
		 */
		public final byte getStatus() {
			return status;
		}

		/**
		 * Erfragt ob die Datenbeschreibung im Moment auf
		 * <code>keine Daten</code> oder <code>keine Quelle</code> steht.
		 *
		 * @return ob die Datenbeschreibung im Moment auf
		 *         <code>keine Daten</code> oder <code>keine Quelle</code> steht
		 */
		public final boolean isImMomentKeineDaten() {
			return imMomentKeineDaten;
		}

	}

	protected ClientDavInterface getDav() {
		return dav;
	}
}
