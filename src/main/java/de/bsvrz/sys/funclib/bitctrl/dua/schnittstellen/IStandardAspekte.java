/*
 * Allgemeine Funktionen f�r das Segment DuA
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;

/**
 * �ber diese Schnittstelle sollen die Standardaspekte f�r die Publikation
 * innerhalb von bestimmten SWE-Modul-Typ-Kombinationen zur Verf�gung gestellt
 * werden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public interface IStandardAspekte {

	/**
	 * Erfragt den Standardaspekt der Publikation f�r ein bestimmtes empfangenes
	 * Originaldatum.
	 *
	 * @param originalDatum
	 *            das Originaldatum
	 * @return der Standardpublikationsaspekt des �bergebenen Datums oder
	 *         <code>null</code>, wenn kein Aspekt ermittelt werden konnte
	 */
	Aspect getStandardAspekt(final ResultData originalDatum);

	/**
	 * Erfragt die Datenanmeldungen, die f�r die Publikation unter den
	 * Standardaspekten durchgef�hrt werden m�ssen.
	 *
	 * @param objektFilter
	 *            die Systemobjekte, die betrachtet werden sollen. Wenn dieser
	 *            Array leer (oder <code>null</code>) ist, werden alle
	 *            definierten Standardanmeldungen zur�ckgegeben. Sonst nur
	 *            solche, die die in diesem Array enthaltenen Systemobjekte
	 *            beinhalten.
	 * @return die Objektanmeldungen, die f�r die Publikation unter den
	 *         Standardaspekten durchgef�hrt werden m�ssen (ggf. leere Menge)
	 */
	Collection<DAVObjektAnmeldung> getStandardAnmeldungen(final Collection<SystemObject> objektFilter);

	/**
	 * Erfragt alle Attributgruppen, die innerhalb dieser
	 * Standardaspekteversorgung beschrieben sind.
	 *
	 * @return eine ggf. leere Collection mit Attributgruppen
	 */
	Collection<AttributeGroup> getAlleAttributGruppen();

}
