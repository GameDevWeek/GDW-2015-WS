package de.hochschuletrier.gdw.ws1516.menu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.events.ShowCreditsEvent;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.game.systems.ScoreSystem;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.states.GameplayState;

public class EndPage extends MenuPage {
    private final String mapToLoad;
    private final MenuManager menuManager;

    public enum Type {

        WIN,
        GAMEOVER
    }
    
    public EndPage(Skin skin, MenuManager menuManager, String background, Type type, ScoreComponent scoreComp, String mapToLoad) {
        super(skin, background);
        this.menuManager = menuManager;
        this.mapToLoad = mapToLoad;

        String message,messageStyle;
        Sound sound;

        if (type == Type.GAMEOVER) {
            message = "Verloren!";
            messageStyle = "gameover";
            Texture endImage = assetManager.getTexture("dead_unicorn_gameover");
            addDecoImage(endImage, Main.WINDOW_WIDTH/2, Main.WINDOW_HEIGHT/2, Align.center, Align.center);
            sound = assetManager.getSound("lose_sound");
        } else {
            Texture bonbons = assetManager.getTexture("drop");
            Texture chocoCoins = assetManager.getTexture("coin_hud");
            Texture clock_hud = assetManager.getTexture("clock_hud");
            message = "Gewonnen!";
            messageStyle = "win";
            sound = assetManager.getSound("win_sound");
            int timeScore = (int) (GameConstants.SCORE_TIME_POINTS - scoreComp.playedSeconds);
            final int chocoScore = scoreComp.chocoCoins * GameConstants.SCORE_CHOCOCOINS_POINTS;
            final int bonbonScore = scoreComp.bonbons * GameConstants.SCORE_BONBONS_POINTS;

            addScoreLine(400, clock_hud, "Level geschafft ", GameConstants.SCORE_BASEPOINTS); // hier fehlt ein made it icon
            addScoreLine(350, chocoCoins, scoreComp.chocoCoins + " (" + GameConstants.SCORE_CHOCOCOINS_POINTS + " Punkte)", chocoScore);
            addScoreLine(300, bonbons, scoreComp.bonbons + " (" + GameConstants.SCORE_BONBONS_POINTS + " Punkte)", bonbonScore);
            addScoreLine(250, clock_hud, "Zeitmalus", timeScore);

            addLabel(600, 325, "" + ( ScoreSystem.getFinalScoreStatic() ), "win");
        }

        addCenteredLabel(0, 480, Main.WINDOW_WIDTH, 50, message, messageStyle);
        SoundEmitter.playGlobal(sound, false);

        addCenteredButton(200, 100, 100, 50, "Ins Hauptmenü", this::stopGame, "menu");
        if(type == Type.GAMEOVER)
            addCenteredButton(Main.WINDOW_WIDTH - 200, 100, 100, 50, "Nochmal versuchen", this::nextLevel, "einhornMotivated");
        else
            addCenteredButton(Main.WINDOW_WIDTH - 200, 100, 100, 50, "Nächstes Level", this::nextLevel, "einhornMotivated");
    }
    
    private void addScoreLine(int y, Texture icon, String label, int value) {
        int x = 250;
        int lineheight = 20;
        addDecoImage(icon, x, y + lineheight*0.5f, Align.center, Align.center);
        addLabel(x + 100, y, label, "default");
        addLabel(x + 230, y, "=", "default");
        addLabel(x + 250, y, "" + value, "default");
    }
    
    private void onOutroSkipped() {
        main.changeState(main.getPersistentState(MainMenuState.class));
        ShowCreditsEvent.emit();
    }

    private void nextLevel() {
        if(mapToLoad == null) {
            menuManager.popPage();
            MenuPageScene outroPage = new MenuPageScene(skin, menuManager, "data/json/endSequence.json", this::onOutroSkipped,"outro_bg");
            menuManager.addLayer(outroPage);
            menuManager.pushPage(outroPage);
        } else {
            Game game = new Game();
            game.init(assetManager, mapToLoad);
            main.changeState(new GameplayState(assetManager, game, assetManager.getMusic(mapToLoad)));
        }
    }

    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));

        }
    }
}
