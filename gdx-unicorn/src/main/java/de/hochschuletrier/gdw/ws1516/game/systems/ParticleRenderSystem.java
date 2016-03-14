package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem.SubSystem;

public class ParticleRenderSystem extends SubSystem {
    public ParticleRenderSystem(Family family) {
        // TODO set family to correct filter
        super(family);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub

    }
}