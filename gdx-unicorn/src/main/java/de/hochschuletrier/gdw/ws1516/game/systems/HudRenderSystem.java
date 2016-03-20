package de.hochschuletrier.gdw.ws1516.game.systems;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.CircularProgressRenderer;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.Game;
import de.hochschuletrier.gdw.ws1516.game.GameConstants;
import de.hochschuletrier.gdw.ws1516.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1516.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PlayerComponent.State;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.ScoreComponent;
import de.hochschuletrier.gdw.ws1516.states.MainMenuState;
import de.hochschuletrier.gdw.ws1516.events.FinalScoreEvent;

public class HudRenderSystem extends IteratingSystem implements FinalScoreEvent.Listener {

    private final AssetManagerX assetManager;
    private final BitmapFont font;
    private long finalScore;
    private final Texture coin;
    private final Texture drop;
    private final Texture hornAttackRdy;
    private final Texture clock;
    private final Texture hearts[] = new Texture[4];
    private final float halfTextHeight;
    private CircularProgressRenderer flyingCooldownRenderer;
    private CircularProgressRenderer dashCooldownRenderer;

    public HudRenderSystem(int priority) {
        super(Family.all(PlayerComponent.class).get(), priority);

        assetManager = Main.getInstance().getAssetManager();
        font = assetManager.getFont("verdana_32");
        halfTextHeight = font.getLineHeight()/2;

        coin = assetManager.getTexture("coin_hud");
        drop = assetManager.getTexture("drop");
        hornAttackRdy = assetManager.getTexture("dash_offCooldown");
        clock = assetManager.getTexture("clock_hud");

        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = assetManager.getTexture("heart" + i);
        }
        Texture hornAttackCd_Ver2 = assetManager.getTexture("dash_Cooldown_V2");
        dashCooldownRenderer = new CircularProgressRenderer(hornAttackCd_Ver2);
        Texture blue_gum = assetManager.getTexture("gum_hud");
        flyingCooldownRenderer = new CircularProgressRenderer(blue_gum);
    }

    @Override
    public void addedToEngine(Engine engine) {
        // TODO Auto-generated method stub
        super.addedToEngine(engine);
        FinalScoreEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        FinalScoreEvent.unregister(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScoreComponent scoreComp = ComponentMappers.score.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        InputComponent inputComp = ComponentMappers.input.get(entity);
        PositionComponent posComp = ComponentMappers.position.get(entity);

        Main.getInstance().screenCamera.bind();

        int displayWidth = Gdx.graphics.getWidth();
        int displayHeight = Gdx.graphics.getHeight();

        float heart_x = 40;
        float heart_y = 40;

        float lives_x = heart_x + 55;
        float lives_y = heart_y + 5;

        float gum_x = lives_x + 80;
        float gum_y = heart_y;

        float gum_count_x = gum_x + 35;
        float gum_count_y = heart_y + 5;

        float time_x = displayWidth - 120;
        float time_y = heart_y;

        float clock_x = time_x + 70;

        float iconRightPos = clock_x;
        
        float hornAttack_x = 60;
        float hornAttack_y = displayHeight - 60;

        int minutes_int = (int) scoreComp.playedSeconds / 60;
        String minutes_string = String.valueOf(minutes_int);
        int seconds_int = (int) scoreComp.playedSeconds % 60;
        String seconds_string = String.valueOf(seconds_int);

        if (seconds_string.length() == 1) {
            seconds_string = "0" + seconds_string;
        }

        int lives = playerComp.lives;
        int gum_count_int = playerComp.blueGumStacks;

        String lives_string = "x " + String.valueOf(lives);
        String gum_count_string = "x " + String.valueOf(gum_count_int);
        String time = minutes_string + ":" + seconds_string;
        String scoreChoco = "" + scoreComp.chocoCoins;
        String scoreBonbon = "" + scoreComp.bonbons;

        int heartIndex = Math.max(0, Math.min(playerComp.hitpoints, hearts.length - 1));
        drawCenteredTexture(hearts[heartIndex], heart_x, heart_y);
        drawText(lives_string, lives_x, lives_y);
        //DrawUtil.draw(blue_gum, gum_x, gum_y, 40, 40);
        drawText(gum_count_string, gum_count_x, gum_count_y);
        drawRightItem(clock, time, iconRightPos, heart_y, -8);
        drawRightItem(coin, scoreChoco, iconRightPos, heart_y + 60, -4);
        drawRightItem(drop, scoreBonbon, iconRightPos, heart_y + 120, -4);
        drawCenteredTexture(hornAttackRdy, hornAttack_x, hornAttack_y);

        //CircularProgressRenderer dashCooldownRenderer = new CircularProgressRenderer(hornAttackCd);
        float hornCooldown;
        if (playerComp.hornAttackCooldown != 0.0f && playerComp.state != State.HORNATTACK) {
            hornCooldown = -(playerComp.hornAttackCooldown / GameConstants.HORN_MODE_COOLDOWN);
        } else {
            hornCooldown = 1.0f;
        }

        if (playerComp.hornAttackCooldown != 0) {
            dashCooldownRenderer.draw(DrawUtil.batch, hornAttack_x, hornAttack_y, 92, 92, hornCooldown * 360.0f);
        } else {
            DrawUtil.draw(hornAttackRdy, hornAttack_x - 46, hornAttack_y - 46, 92, 92);
        }

        float flyingCooldown;
        if (playerComp.flyingCooldown != 0.0f) {
            flyingCooldown = -(playerComp.flyingCooldown / GameConstants.FLYING_TIME);
        } else {
            flyingCooldown = 1.0f;
        }
        flyingCooldownRenderer.draw(DrawUtil.batch, gum_x, gum_y, 40, 40, flyingCooldown * 360.0f);

        float spitState = inputComp.gumSpitCharge;

        if (spitState > 0.0) {
            float chargeBar_x = posComp.x - CameraSystem.getCameraPosition().x + Gdx.graphics.getWidth() / 2 - 40;
            float chargeBar_y = posComp.y - CameraSystem.getCameraPosition().y + Gdx.graphics.getHeight() / 2 - 60;
            float chargeBar_width = 80;
            float chargeBar_height = 15;
            float inside_x = chargeBar_x + 1;
            float inside_y = chargeBar_y + 1;
            float inside_width = chargeBar_width - 1;
            float inside_height = chargeBar_height - 2;
            DrawUtil.drawRect(chargeBar_x, chargeBar_y, chargeBar_width, chargeBar_height, Color.BLACK);
            DrawUtil.fillRect(inside_x, inside_y, spitState * inside_width, inside_height, new Color(0xff / 256F, 0x35 / 256F, 0xD2 / 256F, 1f));
        }
    }

    private void drawRightItem(Texture texture, String value, float x, float y, float iconYOffset) {
        drawCenteredTexture(texture, x, y + iconYOffset);
        drawTextRightAligned(value, x - 40, y);
    }

    private void drawText(String text, float x, float y) {
        font.draw(DrawUtil.batch, text, x, y - halfTextHeight);
    }
    private void drawTextRightAligned(String text, float x, float y) {
        float width = font.getBounds(text).width;
        font.draw(DrawUtil.batch, text, x - width, y - halfTextHeight);
    }
    private void drawCenteredTexture(Texture texture, float x, float y) {
        DrawUtil.draw(texture, x - texture.getWidth()/2, y - texture.getHeight()/2, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void onFinalScoreChanged(long score, ScoreComponent scoreComp) {
        /**
         * @author philipp -> gamelogic scorecomp hat alle werte
         */
        finalScore = score;
    }

}
