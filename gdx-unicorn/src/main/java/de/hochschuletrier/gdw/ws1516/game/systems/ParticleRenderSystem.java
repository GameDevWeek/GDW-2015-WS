package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;
import de.hochschuletrier.gdw.ws1516.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1516.game.components.RenderLayerComponent;

public class ParticleRenderSystem extends SubSystem {
    public ParticleRenderSystem() {
        // TODO set family to correct filter
        super(Family.all(PositionComponent.class).get());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        System.out.println("partikelrender");
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub

    }
}