/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

/**
 *
 * @author Lusito
 */
public enum PlayerColor {
    RED(new Color(160/255.0f, 20/255.0f, 20/255.0f, 1)),
    BLUE(new Color(40/255.0f, 90/255.0f, 170/255.0f, 1)),
    NEUTRAL(Color.WHITE);
    
    public final Color color;
    public AnimationExtended animation;
    public AnimationExtended splashAnimation;
    public AnimationExtended projectileAnimation;
    public ParticleEffect particleEffect;

    PlayerColor(Color color) {
        this.color = color;
    }
}
