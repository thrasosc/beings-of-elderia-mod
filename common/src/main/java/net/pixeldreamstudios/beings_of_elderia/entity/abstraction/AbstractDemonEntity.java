package net.pixeldreamstudios.beings_of_elderia.entity.abstraction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.pixeldreamstudios.beings_of_elderia.entity.constant.DefaultAnimations;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;

import java.util.List;

public abstract class AbstractDemonEntity extends Monster implements SmartBrainOwner<AbstractDemonEntity>, GeoEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    protected AbstractDemonEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 2.5);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "livingController", 5, event -> {
            if ((event.isMoving()) && !swinging) {
                return event.setAndContinue(DefaultAnimations.WALK);
            }
            return event.setAndContinue(DefaultAnimations.IDLE);
        })).add(new AnimationController<>(this, "attackController", 0, event -> {
            swinging = false;
            return PlayState.STOP;
        }).triggerableAnim("attack", DefaultAnimations.ATTACK));
    }

    @Override
    public List<ExtendedSensor<AbstractDemonEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<AbstractDemonEntity>()
                        .setPredicate((target, entity) -> target instanceof Player),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new LookAtTarget(),
                new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<AbstractDemonEntity>(
                        new TargetOrRetaliate<>().alertAlliesWhen((mob, entity) -> this.isAggressive()),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        this.playSound(SoundEvents.VINDICATOR_HURT, 1.0f, 1.5f);
        return null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        this.playSound(SoundEvents.VINDICATOR_AMBIENT, 1.0f, 1.5f);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.VINDICATOR_DEATH, 1.0f, 1.5f);
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 1.0f, 0.75f);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}