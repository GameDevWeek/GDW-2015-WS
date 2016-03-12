Unicorn Bubblegum
===================
1EB = Eine Einhornbreite
1GB = Eine Gegnerbreite

###Input
- Auf dem Boden:
  - Pfeiltasten: Bewegung links/rechts, sprung
  - Taste A: Hornattacke
  - Taste B: Rosa Kaugummi
  - Taste C: Blaues Kaugummi
- Im Flugmodus:
	- Pfeiltasten: Bewegung links/rechts, auf/ab
	- Taste A,B,C: Blase platzen lassen
- Im Fallen:
	- Pfeiltasten: Bewegung links/rechts
	- Taste A,B,C: wirkungslos

### Einhorn
- M�hne f�rbt sich nach aktivem Kaugummi
	- Im Regenbogenmodus knallbunt
- 3 Leben
	- Beim Tod wird ein Leben verbraucht und das Einhorn zum letzten Speicherpunkt bewegt.
	- Hat es keine Leben mehr, so muss es neu anfangen
- 3 Hits
	- Ber�hrt das Einhorn einen Gegner ohne Angriff, so verliert es einen Hit und wird zur�ckgeworfen.
	- Wird das Einhorn von einem Geschoss getroffen verliert es einen Hit.
	- Sind alle Hits aufgebraucht stirbt es.

### Kaugummisorten
- Rosa: Standardkaugummi.
	- Immer verf�gbar.
	- Je l�nger die Taste gedr�ckt wird umso weiter fliegt das Kaugummi (in einem h�heren Bogen)
		- Reichweite: ca. 2-5 EB
		- Gravitation wirkt sich auf das Kaugummi aus.
	- Das Einhorn bleibt dabei stehen und stellt sich mit der Zeit immer weiter auf die Hinterbeine
	- Cooldown: 1s
	- Trifft das Kaugummi auf nichts, so bleibt es liegen
	- Trifft das Kaugummi auf einen Gegner, so werden Gegner im Radius 3GB um die Trefferposition eingeklebt.
		- Die Klebezeit variiert zwischen 1s und 5s abh�ngig von der Entfernung zur Trefferposition
	- Das Kaugummi, liegengeblieben oder am Gegner klebend  blockiert den Weg, so dass Gegner nicht dran vorbei k�nnen. Das Einhorn kann jedoch dr�ber springen.
- Blau: Flugkaugummi.
	- Bei Aktivierung: Kaut f�r 1s, bl�st Kaugummiblase, steigt etwas an und geht in den Flugmodus �ber.

### Hornattacke
- Bei Aktivierung richtet es sein Horn nach vorne und rennt schnell vorw�rts f�r ca. 2 EB.
- Ist ein Gegner im Weg, so wird er aufgespiest und explodiert mit einem Splattereffekt.
- Sind weitere Gegner in 3GB vor dem Einhorn beim Treffen, so sterben diese genauso.
- Cooldown: 3-5s

### Items zum aufsammeln
- Blaues Kaugummi
- Lebenspunkt
- Score Items: Schokotaler, Bonbons, etc.
- Regenbogenkaugummi:
	- Verh�lt sich wie der Mario Stern: Wird direkt aktiviert, schnelleres laufen, automatisches aufspie�en von Gegnern.
	- Dauer: 10s

### Speicherpunkte

- Im Level gibt es Punkte, an dem man respawnen kann wenn man gestorben ist
- Hier werden die Hits zur�ck aufgef�llt

### Gegner
- Allgemein:
	-  Laufen an einem Pfad entlang, bis das Einhorn in sichtreichweite ist (5EB)
	- Geschwindigkeit: ~0,75 Einhorngeschwindigkeit.
	- Sind sie eingeklebt, k�nnen sie sie nicht bewegen und auch nicht schie�en oder blitzen.
	- Keine Kollision mit anderen Gegnern
- Paparazzi:
	- Schie�en Fotos. Blitze �berlagern den Bildschirm. Je n�her am Einhorn umso gr��er werden die Blitze.
	- Sobald in Sichtweite, laufen sie auf das Einhorn zu. 
- J�ger:
	- Sobald in Sichtweite, lehnen sie zum Schuss an (dauert ~1s) und schiessen ein langsames Projektil.
		- Cooldown: 3s.
		- Geschwindigkeit: 0,75 Einhorngeschwindigkeit
		- Das Einhorn kann �ber das Projektil springen
		- Kann in alle Richtungen schie�en
		- Schussanlehnen muss visualisiert werden
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
- Zeit?

### Motivation
Das Einhorn sieht einen T-Rex/Elefanten der versucht, Kaugummiblasen zu machen. Der T-Rex/Elefant schafft es jedoch nicht und das Einhorn will ihm helfen. Dazu muss es durch verschiedene Level kommen

