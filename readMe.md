# Startanleitung für io-simulation-javafx

Diese Anleitung beschreibt, wie du die Anwendung aus der bereitgestellten ZIP-Datei startest.

## Voraussetzungen

- Das Betriebssystem entspricht der Konfiguration in der Maven-POM (z. B. Linux oder macOS, falls `-P !windows` gesetzt ist).
- Falls du die Anwendung auf Windows startest, passe ggf. die Konfiguration an.

## Inhalt der ZIP-Datei

Die ZIP-Datei enthält ein selbstständiges, mittels [jlink](https://openjdk.java.net/jeps/282) erstelltes Laufzeit-Image. Dieses Image beinhaltet:
- Eine minimalistische Java Runtime Environment (JRE)
- Alle benötigten Bibliotheken und Ressourcen für den Betrieb der Anwendung
- Ein Launcher-Skript, das die Anwendung startet

## Startvorgang

1. **ZIP-Datei entpacken**  
   Entpacke die Datei `io-simulation-javafx.zip` in ein gewünschtes Verzeichnis.

2. **In das Image-Verzeichnis wechseln**  
   Navigiere im Terminal oder in der Eingabeaufforderung in das entpackte Verzeichnis. Dort findest du einen Ordner, der üblicherweise den Namen `bin` enthält.

3. **Anwendung starten**
    - **Unter Linux/macOS:**  
      Öffne ein Terminal, wechsle in das `bin`-Verzeichnis und führe das Startskript aus:
      ```bash
      ./io-simulation-javafx
      ```
    - **Unter Windows:**  
      Gehe in das `bin`-Verzeichnis und starte die Anwendung über die `io-simulation-javafx.exe` (sofern vorhanden). Falls kein EXE erstellt wurde, starte die Anwendung über die entsprechende Batch-Datei:
      ```batch
      io-simulation-javafx.bat
      ```

4. **Anwendung verwenden**  
   Nach dem Start öffnet sich die Anwendung. Die Hauptklasse `io.simulation.Main` wird automatisch als Einstiegspunkt genutzt.

## Hinweis zur Erstellung

Die Anwendung wurde mit dem [JavaFX Maven Plugin](https://openjfx.io/openjfx-docs/) in Version `0.0.8` konfiguriert. Wichtige Parameter in der `pom.xml` waren:
- **`<launcher>`** und **`<jlinkImageName>`**: `io-simulation-javafx`
- **`<jlinkZipName>`**: `io-simulation-javafx`
- **`<mainClass>`**: `io.simulation.Main`

Diese Einstellungen sorgen dafür, dass ein lauffähiges Image samt Startskript erstellt wird.

Viel Spaß mit der Anwendung!
