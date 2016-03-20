package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1516.events.ChangeInGameStateEvent;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;
import de.hochschuletrier.gdw.ws1516.events.ShowCreditsEvent;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;

public class MainMenuPage extends MenuPage implements ShowCreditsEvent.Listener {

    private final MenuManager menuManager;
    private LevelSelectionPage levelSelectionPage;
    private final MenuPageCredits menuPageCredits;

    public enum Type {

        MENU,
        PAUSED

    }

    public MainMenuPage(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");
        this.menuManager = menuManager;

        int i = 0;
        int xOffset = 55;
        int yOffset = 370;
        int yStep = 55;

        if (type == Type.MENU) {
            ShowCreditsEvent.register(this);
            levelSelectionPage = new LevelSelectionPage(skin, menuManager);
            menuManager.addLayer(levelSelectionPage);
            MenuPageScene introPage = new MenuPageScene(skin, menuManager, "data/json/intro.json", this::onIntroSkipped,"intro_bg");
            addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Spiel starten", introPage);
            //addLeftAlignedButton(xOffset, yOffset - yStep *( i++), 150, 50, "Start Game", this::startGame);
        } else if (type == Type.PAUSED) {
            addLeftAlignedButton(xOffset, yOffset - yStep * (i++), 150, 50, "Fortsetzen", () -> {
                menuManager.popPage();
            }, "buttonSound");
        }
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Optionen", new MenuPageOptions(skin, menuManager));
        menuPageCredits = new MenuPageCredits(skin, menuManager);
        addPageEntry(menuManager, xOffset, yOffset - yStep * (i++), "Credits", menuPageCredits);

        if (type == Type.MENU) {
            //addLeftAlignedButton(xOffset, yOffset - yStep *( 2* i++), 100, 50, "Beenden", () -> System.exit(-1),"einhornEmpathy");
            addLeftAlignedButton(xOffset, yOffset - yStep * (2 * i++), 100, 50, "Beenden", this::systemExitDelay, "einhornEmpathy");

        } else if (type == Type.PAUSED) {
            addLeftAlignedButton(xOffset, yOffset - yStep * (2 * i++), 100, 50, "Menü", this::stopGame, "menu");
        }
    }
    
    @Override
    public void onShowCreditsEvent() {
        menuManager.popAllPages();
        menuManager.pushPage(menuPageCredits);
    }

    private void onIntroSkipped() {
        menuManager.popPage();
        menuManager.pushPage(levelSelectionPage);
    }

    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));

        }

    }

    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);

        addLeftAlignedButton(x, y, 150, 40, text, () -> menuManager.pushPage(page), "buttonSound");
    }

    private void systemExitDelay() {

        try {
            Thread.sleep(1300);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.exit(-1);
    }

}
