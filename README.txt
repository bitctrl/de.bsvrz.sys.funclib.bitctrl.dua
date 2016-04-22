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
