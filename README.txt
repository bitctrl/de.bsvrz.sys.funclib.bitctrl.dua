********************************************************************************
*                        Allgemeine DuA-Funktionen                             *
********************************************************************************

Version: ${version}

�bersicht
=========

Dieses Modul enth�lt allgemeine Funktionen die von mehreren SWE des Segments DuA
genutzt werden oder potentiell genutzt werden k�nnen.


Versionsgeschichte
==================

1.5.0
=====
- Kompatibilit�t mit Parallel-Entwicklung der DuA-SWE wiederhergestellt

1.4.0
=====
- Abspaltung des Komponente von Funclib-BitCtrl
- �bernahme der �nderungen der Firma Kappich im Rahmen der DuA-�berarbeitung

DuaKonstanten:
- neue Konstante f�r ATG "atg.messQuerschnittVirtuell"

AbstraktVerwaltungsAdapter:
- Betriebsmeldung beim Fehlschlagen der Initialisierung entfernt

MessQuerschnittVirtuell:
- Anteile des VMQ als eigene Klasse "MessQuerschnittAnteile" ausgelagert
- Debug-Level im Konstruktor auf FINE gesetzt, wenn die ATG "virtuellStandard" nicht versorgt ist
- Funktionsnamen f�r die ermittlung der MessQuerschnittAnteile angepasst

AtgMessQuerschnittVirtuell erg�nzt
Schnittstelle MessQuerschnittAnteile erg�nzt

AtgMessQuerschnittVirtuellVLage
- erweitert die neue Schnittstelle MessQuerschnittAnteile
- Ist der Messquerschnitt von dem die Geschwindigkeit uebernommen werden soll nicht explizit versorgt wird nicht mehr der erste aus der Liste der Anteile genommen
- getMessQuerschnittGeschwindigkeit liefert gegebenenfalls null und nicht den erstbesten MQ

DUAUmfeldDatenSensor
- verwendet eine IndentityHashMap f�r die Verwaltung der Instanzen

UmfeldDatenArt
- hashCode-Funktion erg�nzt


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
Wei�enfelser Stra�e 67
04229 Leipzig
Phone: +49 341-490670
mailto: info@bitctrl.de
