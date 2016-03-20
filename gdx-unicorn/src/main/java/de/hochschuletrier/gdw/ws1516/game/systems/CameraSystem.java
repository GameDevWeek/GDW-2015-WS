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

    private static final LimitedSmoothCamera camera;
    
    private float targetX;
    private float targetY;
    
    static {
        camera = new LimitedSmoothCamera();
    }
    
    @SuppressWarnings("unchecked")
    public CameraSystem(int priority) {
        super(Family.all(CameraTargetComponent.class).get(), priority);
        
        this.targetX = this.targetY = 0.0f;
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
    
    public void bindCamera() {
        camera.bind();
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent targetPos = ComponentMappers.position.get(entity);
        targetX = targetPos.x;
        targetY = targetPos.y;
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        camera.setDestination(targetX, targetY);
        camera.update(deltaTime);
        camera.bind();
    }

    /**
     * @return camera position in world coordinates
     */
    public static Vector2 getCameraPosition() {
        return new Vector2(camera.getPosition().x, camera.getPosition().y);
    }
    
    /**
     * @return camera viewport top left corner in world coordinates
     */
    public static Vector2 getViewportTopLeft() {
        return new Vector2( camera.getLeftOffset() , camera.getTopOffset() );
    }
    
    /**
     * @return camera viewport top right corner in world coordinates
     */
    public static Vector2 getViewportTopRight() {
        return new Vector2( camera.getLeftOffset() + Gdx.graphics.getWidth(), camera.getTopOffset());
    }
    
    /**
     * @return camera viewport bottom right corner in world coordinates
     */
    public static Vector2 getViewportBottomRight() {
        return new Vector2( camera.getLeftOffset() + Gdx.graphics.getWidth() , camera.getTopOffset() + Gdx.graphics.getHeight());
    }
    
    /**
     * @return camera viewport bottom left corner in world coordinates
     */
    public static Vector2 getViewportBottomLeft() {
        return new Vector2( -camera.getLeftOffset() , camera.getTopOffset() + Gdx.graphics.getHeight() );
    }
    
    public static Vector2 worldToScreenCoordinates(Vector2 world) {
        Vector2 out = new Vector2( Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        return out.sub(getCameraPosition()).add(world);
    }
    
    public static Vector2 screenToWorldCoordinates(Vector2 screen) {
        Vector2 out = new Vector2(- Gdx.graphics.getWidth() / 2, - Gdx.graphics.getHeight() / 2);
        return out.add(getCameraPosition()).add(screen);
    }
    
    public static Vector2 worldToScreenCoordinates(float worldX, float worldY) {
        return worldToScreenCoordinates(new Vector2(worldX, worldY));
    }
    
    public static Vector2 screenToWorldCoordinates(float screenX, float screenY) {
        return worldToScreenCoordinates(new Vector2(screenX, screenY));
    }
}
