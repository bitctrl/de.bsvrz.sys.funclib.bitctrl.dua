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

package de.bsvrz.sys.funclib.bitctrl.dua.adapter;

import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVSendeAnmeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DatenFlussSteuerungsVersorger;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IBearbeitungsKnoten;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * Adapterklasse für einen Bearbeitungsknoten.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class AbstraktBearbeitungsKnotenAdapter implements IBearbeitungsKnoten {

	/**
	 * nächster Bearbeitungsknoten.
	 */
	protected IBearbeitungsKnoten knoten;

	/**
	 * <b>FLAG</b>: Soll publiziert werden?
	 */
	protected boolean publizieren;

	/**
	 * Verbindung zum Verwaltungsmodul.
	 */
	protected IVerwaltung verwaltung;

	/**
	 * Schnittstelle zu den Informationen über die Standardpublikationsaspekte.
	 */
	protected IStandardAspekte standardAspekte;

	/**
	 * Anmeldungen zum Publizieren von verarbeiteten Daten.
	 */
	protected DAVSendeAnmeldungsVerwaltung publikationsAnmeldungen;

	@Override
	public void setPublikation(final boolean publizieren1) {
		publizieren = publizieren1;
	}

	@Override
	public void setNaechstenBearbeitungsKnoten(final IBearbeitungsKnoten knoten1) {
		knoten = knoten1;
	}

	@Override
	public void initialisiere(final IVerwaltung dieVerwaltung) throws DUAInitialisierungsException {
		if ((dieVerwaltung == null) || (dieVerwaltung.getVerbindung() == null)) {
			throw new DUAInitialisierungsException("Es konnte keine Verbindung"
					+ " zum Verwaltungsmodul (bzw. zum Datenverteiler" + ") hergestellt werden");
		}
		verwaltung = dieVerwaltung;
		publikationsAnmeldungen = new DAVSendeAnmeldungsVerwaltung(verwaltung.getVerbindung(), SenderRole.source());
		DatenFlussSteuerungsVersorger.getInstanz(verwaltung).addListener(this);
	}

	@Override
	public String toString() {
		return "Modul-Typ: " + (getModulTyp() != null ? getModulTyp() : "unbekannt") + " in SWE "
				+ verwaltung.getSWETyp();
	}

	/**
	 * liefert das dem Knoten zugeordnete Verwaltungsmodul.
	 *
	 * @return das Modul
	 */
	public IVerwaltung getVerwaltung() {
		return verwaltung;
	}

	/**
	 * liefert den über den Adapter bereitgestellten Knoten.
	 *
	 * @return den Knoten
	 */
	public IBearbeitungsKnoten getKnoten() {
		return knoten;
	}

	/**
	 * ermittelt, ob die Ergebnisse der Verarbeitung in diesem Knoten publiziert
	 * werden sollen.
	 *
	 * @return den Zustand
	 */
	public boolean isPublizieren() {
		return publizieren;
	}

	protected DAVSendeAnmeldungsVerwaltung getPublikationsAnmeldungen() {
		return publikationsAnmeldungen;
	}

	protected IStandardAspekte getStandardAspekte() {
		return standardAspekte;
	}

	protected void setStandardAspekte(final IStandardAspekte standardAspekte) {
		this.standardAspekte = standardAspekte;
	}
}
