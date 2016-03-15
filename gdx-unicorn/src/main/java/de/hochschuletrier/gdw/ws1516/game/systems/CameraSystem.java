package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.DummyUnicornComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class CameraSystem extends IteratingSystem {

    private final LimitedSmoothCamera camera;
    
    @SuppressWarnings("unchecked")
    public CameraSystem(int priority) {
        super(Family.all(DummyUnicornComponent.class).get(), priority);
        
        this.camera = new LimitedSmoothCamera();
    }
    
//    public LimitedSmoothCamera getCamera() {
//        return camera;
//    }
    
    @Override
    public void addedToEngine(Engine engine) {
        
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Main.getInstance().addScreenListener(camera);
        
        super.addedToEngine(engine);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
        Main.getInstance().removeScreenListener(camera);
        
        super.removedFromEngine(engine);
    }
    
    
    public void setCameraBounds(int xMin, int yMin, int xMax, int yMax) {
        camera.setBounds(xMin, yMin, xMax, yMax);
        camera.updateForced();
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent playerPos = ComponentMappers.position.get(entity);
        
        camera.setDestination(playerPos.x, playerPos.y);
        camera.update(deltaTime);
        camera.bind();
    }
}
