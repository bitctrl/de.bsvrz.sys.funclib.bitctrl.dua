Versionsverlauf
===============

## [Noch nicht veröffentlicht]

## [Version 1.9.1]

Statische Map in DUAUmfelddatenSensor eliminiert

## [Version 1.9.0]

Statische Intialisierung des DuAVerkehrNetz entfernt

- Statische Methoden als deprecated markiert
- Kompatibiltätsmechanismus implementiert

## [Version 1.8.0]

Zusätzliche Datenart "GlätteAlarmStatus" ergänzt

## [Version 1.7.0]

Zusätzliche Datenarten "ZeitreserveEisglätte" und "ZeitreserveReifglätte" ergänzt

## [Version 1.6.1]

de.bsvrz.sys.funclib.bitctrl.dua.AllgemeinerDatenContainer:

- Die Klasse sollte nicht mehr verwendet werden, weil der
  API-Kontrakt für die Implementierung von equals und hashCode
  verletzt wird und auch nicht sinnvoll implementiert werden kann.
  Für die hashCode-Methode wird hier die hashCode-Methode der
  Superklasse aufgerufen und eine entsprechende Fehlermeldung
  ausgegeben. Die Verwendung der Klasse in Set und Maps als Key
  führt aber zu undefiniertem Verhalten.
	
de.bsvrz.sys.funclib.bitctrl.dua.GanzZahl:
de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorWert:
de.bsvrz.sys.funclib.bitctrl.dua.MesswertMarkierung:

- equals ist implementiert, aber nicht "hashCode". Die equals-Methode wurde entfernt, das sie
  nicht verwendet wird 

- Obsolete SVN-Tags aus Kommentaren entfernt

## [Version 1.6.0]

- Umstellung auf Java 8 und UTF-8

## [Version 1.5.0]

- Kompatibilität mit Parallel-Entwicklung der DuA-SWE wiederhergestellt

## [Version 1.4.0]

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

[Noch nicht veröffentlicht]: https://gitlab.nerz-ev.de/uwe.peuker/de.bsvrz.gradle.nerzswe/compare/v1.9.1...HEAD
[Version 1.9.1]: 
https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.sys.funclib.bitctrl.dua/compare/v1.9.0...v1.9.1
[Version 1.9.0]: 
https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.sys.funclib.bitctrl.dua/compare/v1.8.0...v1.9.0
[Version 1.8.0]: 
https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.sys.funclib.bitctrl.dua/compare/v1.7.0...v1.8.0
[Version 1.7.0]: 
https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.sys.funclib.bitctrl.dua/compare/v1.6.1...v1.7.0
[Version 1.6.1]: 
https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.sys.funclib.bitctrl.dua/compare/v1.6.0...v1.6.1
[Version 1.6.0]: 
https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.sys.funclib.bitctrl.dua/compare/v1.5.0...v1.6.0
