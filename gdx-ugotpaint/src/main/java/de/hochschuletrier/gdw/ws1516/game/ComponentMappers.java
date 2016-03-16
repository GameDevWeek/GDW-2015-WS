package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.ashley.core.ComponentMapper;
import de.hochschuletrier.gdw.ws1516.game.components.*;

public class ComponentMappers {
    public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
    public static final ComponentMapper<PickupComponent> pickup = ComponentMapper.getFor(PickupComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<ProjectileComponent> projectile = ComponentMapper.getFor(ProjectileComponent.class);
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final int PATH_STEP_SIZE = 10;
}
