package io.simulation;

import io.simulation.model.MainModel;
import io.simulation.screens.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private final MainController mainController;
    private final MainModel mainModel;

    // Screens werden nun in einer Map, Key = screenId (z.B. "0", "L", "D", ...)
    private Map<String, Screen> screens = new HashMap<>();

    public ScreenManager(MainController mc, MainModel mm) {
        this.mainController = mc;
        this.mainModel = mm;

        // Screens registrieren
        screens.put("0", new ScreenIO(mm, mc));
        screens.put("L", new ScreenLog());
        screens.put("D", new ScreenDebug());
        screens.put("1", new ScreenAlarmclock(mm, mc));
        screens.put("2", new ScreenSeesaw(mm, mc));
        // usw. - weitere Screens möglich
    }

    /**
     * Leitet einkommende "Setter"-Nachrichten an den passenden Screen weiter.
     * Falls die screenId unbekannt ist, wird hier als Fallback Screen "D" (Debug) genutzt.
     */
    public void handleSetterMessage(String screenId, String line) {
        if (screens.containsKey(screenId)) {
            Screen screen = screens.get(screenId);
            screen.handleIncomingData(line);
        } else {
            // Falls kein passender Screen existiert -> an Debug-Screen schicken
            Screen debugScreen = screens.get("D");
            if (debugScreen != null) {
                debugScreen.handleIncomingData("Unknown screenId '" + screenId + "' => " + line);
            }
        }
    }

    /**
     * Blendet den gewünschten Screen in einem AnchorPane ein.
     * Du kannst diese Methode nutzen, um z.B. Screens dynamisch in der MainView anzuzeigen.
     */
    public void showScreen(String screenId, AnchorPane container) {
        Screen screen = screens.get(screenId);
        if (screen == null) {
            System.err.println("ScreenManager: Unbekannte screenId '" + screenId + "'");
            return;
        }

        Pane view = screen.getView();
        container.getChildren().setAll(view);

        // Damit der Screen das ganze AnchorPane ausfüllt:
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
    }
}
