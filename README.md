[![Build Status](https://travis-ci.org/bitctrl/de.bsvrz.sys.funclib.bitctrl.dua.svg?branch=develop)](https://travis-ci.org/bitctrl/de.bsvrz.sys.funclib.bitctrl.dua)
[![Build Status](https://api.bintray.com/packages/bitctrl/maven/de.bsvrz.sys.funclib.bitctrl.dua/images/download.svg)](https://bintray.com/bitctrl/maven/de.bsvrz.sys.funclib.bitctrl.dua)

********************************************************************************
*                        Allgemeine DuA-Funktionen                             *
********************************************************************************

Version: ${version}

Übersicht
=========

Dieses Modul enthält allgemeine Funktionen die von mehreren SWE des Segments DuA
genutzt werden oder potentiell genutzt werden können.


Versionsgeschichte
==================

1.6.1
=====

de.bsvrz.sys.funclib.bitctrl.dua.AllgemeinerDatenContainer:

	die Klasse sollte nicht mehr verwendet werden, weil der
	API-Kontrakt für die Implementierung von equals und hashCode
	verletzt wird und auch nicht sinnvoll implementiert werden kann.
	Für die hashCode-Methode wird hier die hashCode-Methode der
	Superklasse aufgerufen und eine entsprechende Fehlermeldung
	ausgegeben. Die Verwendung der Klasse in Set und Maps als Key
	führt aber zu undefiniertem Verhalten.

- Obsolete SVN-Tags aus Kommentaren entfernt

1.6.0
=====
- Umstellung auf Java 8 und UTF-8

1.5.0
=====
- Kompatibilität mit Parallel-Entwicklung der DuA-SWE wiederhergestellt

1.4.0
=====
- Abspaltung des Komponente von Funclib-BitCtrl
- Übernahme der Änderungen der Firma Kappich im Rahmen der DuA-Überarbeitung

DuaKonstanten:
- neue Konstante für ATG "atg.messQuerschnittVirtuell"

AbstraktVerwaltungsAdapter:
- Betriebsmeldung beim Fehlschlagen der Initialisierung entfernt

MessQuerschnittVirtuell:
- Anteile des VMQ als eigene Klasse "MessQuerschnittAnteile" ausgelagert
- Debug-Level im Konstruktor auf FINE gesetzt, wenn die ATG "virtuellStandard" nicht versorgt ist
- Funktionsnamen für die ermittlung der MessQuerschnittAnteile angepasst

AtgMessQuerschnittVirtuell ergänzt
Schnittstelle MessQuerschnittAnteile ergänzt

AtgMessQuerschnittVirtuellVLage
- erweitert die neue Schnittstelle MessQuerschnittAnteile
- Ist der Messquerschnitt von dem die Geschwindigkeit uebernommen werden soll nicht explizit versorgt wird nicht mehr der erste aus der Liste der Anteile genommen
- getMessQuerschnittGeschwindigkeit liefert gegebenenfalls null und nicht den erstbesten MQ

DUAUmfeldDatenSensor
- verwendet eine IndentityHashMap für die Verwaltung der Instanzen

UmfeldDatenArt
- hashCode-Funktion ergänzt


Bemerkungen
===========

Das Modul stellt eine Softwarebibliothek dar. Die JAR-Datei muss zur Benutzung
lediglich im Klassenpfad der Anwendung aufgenommen werden. Die Beschreibung der
Schnittstelle kann in der API-Dokumentation nachgelesen werden.


Disclaimer
==========

DuA-Funktionsbibliothek
Copyright (C) 2007 BitCtrl Systems GmbH

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 51
Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.


Kontakt
=======

BitCtrl Systems GmbH
Weißenfelser Straße 67
04229 Leipzig
Phone: +49 341-490670
mailto: info@bitctrl.de
