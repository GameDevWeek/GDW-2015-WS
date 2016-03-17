package de.hochschuletrier.gdw.ws1516.game;

import com.badlogic.ashley.core.ComponentMapper;
import de.hochschuletrier.gdw.commons.gdx.physix.components.*;
import de.hochschuletrier.gdw.ws1516.game.components.*;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TriggerComponent> trigger = ComponentMapper.getFor(TriggerComponent.class);
    public static final ComponentMapper<PhysixBodyComponent> physixBody = ComponentMapper.getFor(PhysixBodyComponent.class);
    public static final ComponentMapper<PhysixModifierComponent> physixModifier = ComponentMapper.getFor(PhysixModifierComponent.class);
    public static final ComponentMapper<BubblegumSpitComponent> bubblegumSpitComponent = ComponentMapper.getFor(BubblegumSpitComponent.class);
    public static final ComponentMapper<BulletComponent> bulletComponent = ComponentMapper.getFor(BulletComponent.class);
    public static final ComponentMapper<ImpactSoundComponent> impactSound = ComponentMapper.getFor(ImpactSoundComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<RenderLayerComponent> renderLayer = ComponentMapper.getFor(RenderLayerComponent.class);
    public static final ComponentMapper<ParticleComponent> particleTest = ComponentMapper.getFor(ParticleComponent.class);
    public static final ComponentMapper<BackgroundParticleComponent> bgParticle = ComponentMapper.getFor(BackgroundParticleComponent.class);
    public static final ComponentMapper<ScoreComponent> score = ComponentMapper.getFor(ScoreComponent.class);
    public static final ComponentMapper<StartPointComponent> startPoint = ComponentMapper.getFor(StartPointComponent.class);
    public static final ComponentMapper<SoundEmitterComponent> soundEmitter = ComponentMapper.getFor(SoundEmitterComponent.class); ;
    public static final ComponentMapper<PathComponent> path = ComponentMapper.getFor(PathComponent.class);
    public static final ComponentMapper<HitPointsComponent> hp = ComponentMapper.getFor(HitPointsComponent.class);
    public static final ComponentMapper<LiveComponent> lives = ComponentMapper.getFor(LiveComponent.class);
    public static final ComponentMapper<EnemyBehaviourComponent> enemyBehaviour = ComponentMapper.getFor(EnemyBehaviourComponent.class);
    public static final ComponentMapper<EnemyTypeComponent> enemyType = ComponentMapper.getFor(EnemyTypeComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class) ;
 
    public static final ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);

    public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
    
}
