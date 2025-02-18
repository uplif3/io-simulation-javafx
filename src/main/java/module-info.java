module io.simulation {
    // Benötigte JavaFX-Module (füge hier weitere hinzu, wenn nötig)
    requires javafx.controls;
    requires javafx.fxml;

    // Falls jSerialComm ein Modul ist, kannst du das angeben, oder lasse es falls nicht modular
    requires com.fazecast.jSerialComm;

    // Exportiere dein Hauptpaket (passe den Namen an)
    exports io.simulation;
    opens io.simulation.controller to javafx.fxml;
    opens io.simulation.view.segment to javafx.fxml;
}
