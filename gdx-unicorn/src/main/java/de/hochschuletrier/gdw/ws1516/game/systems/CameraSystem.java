package de.hochschuletrier.gdw.ws1516.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1516.Main;
import de.hochschuletrier.gdw.ws1516.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1516.game.components.CameraTargetComponent;
import de.hochschuletrier.gdw.ws1516.game.components.PositionComponent;

public class CameraSystem extends IteratingSystem {

    private final LimitedSmoothCamera camera;
    
    @SuppressWarnings("unchecked")
    public CameraSystem(int priority) {
        super(Family.all(CameraTargetComponent.class).get(), priority);
        
        this.camera = new LimitedSmoothCamera();
    }
    
    public void bind(){
        camera.bind();
    }
    
    
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
    
    private Vector2 getCameraPosition() {
        return new Vector2(camera.getPosition().x , camera.getPosition().y);
    }
    
    public Vector2 worldToScreenCoordinates(Vector2 world) {
        Vector2 out = new Vector2( Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        return out.sub(getCameraPosition()).add(world);
    }
    
    public Vector2 screenToWorldCoordinates(Vector2 screen) {
        Vector2 out = new Vector2(- Gdx.graphics.getWidth() / 2, - Gdx.graphics.getHeight() / 2);
        return out.add(getCameraPosition()).add(screen);
    }
}
