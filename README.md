Unicorn Bubblegum
===================
1EB = Eine Einhornbreite
1GB = Eine Gegnerbreite

### Input
- Auf dem Boden:
  - Pfeiltasten: Bewegung links/rechts, Sprung
  - Taste A: Hornattacke
  - Taste B: Rosa Kaugummi
  - Taste C: Blaues Kaugummi
  - Geschwindigkeit beim Laufen: tbd
- Im Flugmodus:
	- Pfeiltasten: Bewegung links/rechts, auf/ab
	- Taste A,B,C: Blase platzen lassen
	- Geschwindigkeit beim Fliegen: 75% der Laufgeschwindigkeit
- Im Fallen:
	- Pfeiltasten: Bewegung links/rechts
	- Taste A,B,C: wirkungslos

### Einhorn
- Mähne färbt sich nach aktivem Kaugummi
	- Im Regenbogenmodus knallbunt
- 3 Leben
	- Beim Tod wird ein Leben verbraucht und das Einhorn zum letzten Speicherpunkt bewegt.
	- Hat es keine Leben mehr, so muss es neu anfangen
- 3 Hits
	- Berührt das Einhorn einen Gegner ohne Angriff, so verliert es einen Hit und wird zurückgeworfen.
	- Wird das Einhorn von einem Geschoss getroffen verliert es einen Hit.
	- Sind alle Hits aufgebraucht stirbt es.

### Kaugummisorten
- Rosa: Standardkaugummi.
	- Immer verfügbar.
	- Je länger die Taste gedrückt wird umso weiter fliegt das Kaugummi (in einem höheren Bogen)
		- Reichweite: ca. 2-5 EB
		- Gravitation wirkt sich auf das Kaugummi aus.
	- Das Einhorn bleibt dabei stehen und stellt sich mit der Zeit immer weiter auf die Hinterbeine
	- Cooldown: 1s
	- Trifft das Kaugummi auf nichts, so bleibt es liegen
	- Trifft das Kaugummi auf einen Gegner, so werden Gegner im Radius 3GB um die Trefferposition eingeklebt.
		- Die Klebezeit variiert zwischen 1s und 5s abhängig von der Entfernung zur Trefferposition
	- Das Kaugummi, liegengeblieben oder am Gegner klebend  blockiert den Weg, so dass Gegner nicht dran vorbei können. Das Einhorn kann jedoch drüber springen.
- Blau: Flugkaugummi.
	- Bei Aktivierung: Kaut für 1s, bläst Kaugummiblase, steigt etwas an und geht in den Flugmodus über.
	- Man fliegt für eine Dauer von 10s
	- Fluggeschwindigkeit 75 % der Laufgeschwindigkeit

### Hornattacke
- Bei Aktivierung richtet es sein Horn nach vorne und rennt schnell vorwärts für ca. 2 EB.
- Ist ein Gegner im Weg, so wird er aufgespießt und explodiert mit einem Splattereffekt.
- Sind weitere Gegner in 3GB vor dem Einhorn beim Treffen, so sterben diese genauso.
- Cooldown: 3-5s

### Items zum aufsammeln
- Blaues Kaugummi (vor allem vor schwierigen Levelabschnitten)
- Lebenspunkt (max. 2 pro Level aufsammelbar)
- Score Items: Schokotaler (1 Punkt), Bonbons (3 Punkte)
- Regenbogenkaugummi:
	- Verhält sich wie der Mario Stern: Wird direkt aktiviert, schnelleres laufen, automatisches aufspießen von Gegnern.
	- Dauer: 10s

### Speicherpunkte

- Im Level gibt es Punkte, an dem man respawnen kann wenn man gestorben ist
- Hier werden die Hits zurück aufgefüllt

### Gegner
- Allgemein:
	- Laufen an einem Pfad entlang, bis das Einhorn in sichtreichweite ist (5EB)
	- Geschwindigkeit: ~0,75 Einhorngeschwindigkeit.
	- Sind sie eingeklebt, können sie sie nicht bewegen und auch nicht schießen oder blitzen.
	- Keine Kollision mit anderen Gegnern
- Paparazzi:
	- Schießen Fotos. Blitze überlagern den Bildschirm. Je näher am Einhorn umso größer werden die Blitze.
	- Sobald in Sichtweite, laufen sie auf das Einhorn zu. 
	- Treten nur in Gruppen auf.
- Jäger:
	- Sobald in Sichtweite, legen sie zum Schuss an (dauert ~1s) und schießen ein langsames Projektil.
		- Cooldown: 3s.
		- Geschwindigkeit: 0,75 Einhorngeschwindigkeit
		- Das Einhorn kann über das Projektil springen
		- Kann in alle Richtungen schießen
		- Schuß anlegen muss visualisiert werden
		- Keine Auswirkungen durch Gravitation

### Welt
- Visuell wie z.B. [Sugar Rush aus dem Film "Wreck it Ralph"](http://wreckitralph.wikia.com/wiki/Candy_Cane_Forest?file=Ralph_meets_Vanellope)
- Todeszonen (Schokofluss, etc.)
- [Bewegliche Platformen]
- [Jumppad]

### HUD
- Leben, Hitpoints
- Score
- Anzahl blaue Kaugummis
- Anzahl Scoreitems
- Zeit

Highscore Berechnung:
- Anzahl der Schokomünzen (1 Punkt pro Münze)
- Anzahl der Bonbons (3 Punkte pro Bonbon)
- Berechnung: 1000 - (Zeit * 100) - (Tode * 50) + (Schokomünzen + Bonbons) * 3 = Highscore

### Menü
- Startmenü (Spiel starten, Optionen, Highscore, Spiel beenden)
            - Spiel starten (Levelauswahlbildschirm, dann Klick auf Spiel starten mit ausgewähltem Level)
            - Optionen (Lautstärke Sounds/Musik ändern, Steuerung anschauen)
            - Highscore (Anzeige der Bestenliste)
            - Spiel beenden (beendet das Spiel)

 - Ingamemenü (Spiel fortsetzen, Levelauswahl, Optionen, Zum Hauptmenü, Spiel beenden)
            - Spiel fortsetzen (Spiel wird fortgesetzt)
            - Levelauswahl (wie beim Startmenü, Spiel wird aber nicht beendet)
            - Optionen (Lautstärke Sounds/Musik ändern, Steuerung anschauen,
            - Zum Hauptmenü (beendet das aktuelle Spiel und ladet das Startmenü)
            - Spiel beenden (Beendet das Spiel)

### Motivation
Das Einhorn sieht einen T-Rex der versucht, Kaugummiblasen zu machen. Der T-Rex schafft es jedoch nicht und das Einhorn will ihm helfen. Dazu muss es durch verschiedene Level kommen

