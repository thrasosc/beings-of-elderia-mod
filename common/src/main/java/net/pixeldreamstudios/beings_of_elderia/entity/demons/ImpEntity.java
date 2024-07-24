package net.pixeldreamstudios.beings_of_elderia.entity.demons;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.AbstractDemonEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.constant.DefaultElderiaAnimations;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

public class ImpEntity extends AbstractDemonEntity {
    protected static final EntityDataAccessor<Boolean> AXE_WIELDER = SynchedEntityData.defineId(ImpEntity.class, EntityDataSerializers.BOOLEAN);
    private static final String[] animations = {"claw_attack_right", "claw_attack_left", "claw_attack_double"};
    public static final RawAnimation CLAW_ATTACK_RIGHT = RawAnimation.begin().then(animations[0], Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation CLAW_ATTACK_LEFT = RawAnimation.begin().then(animations[1], Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation CLAW_ATTACK_DOUBLE = RawAnimation.begin().then(animations[2], Animation.LoopType.PLAY_ONCE);

    public ImpEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.navigation = new SmoothGroundNavigation(this, level());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityNbt) {
        setAxeWielder(RandomUtil.fiftyFifty());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
    }

    private void setAxeWielder(boolean b) {
        this.entityData.set(AXE_WIELDER, b);
        if (b) this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_AXE));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AXE_WIELDER, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsAxeWielder", this.entityData.get(AXE_WIELDER));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(AXE_WIELDER, nbt.getBoolean("IsAxeWielder"));
    }

    public boolean isAxeWielder() {
        return this.entityData.get(AXE_WIELDER);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, BeingsOfElderia.config.impHealth)
                .add(Attributes.ATTACK_DAMAGE, BeingsOfElderia.config.impAttackDamage)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.FOLLOW_RANGE, 64);
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((target, entity) -> !target.isAlive() || !entity.hasLineOfSight(target)),
                new SetWalkTargetToAttackTarget<>()
                        .speedMod((mob, livingEntity) -> isAxeWielder() ? 1.2f : 1.5f),
                new AnimatableMeleeAttack<>(12)
                        .attackInterval(mob -> isAxeWielder() ? 40 : 20)
                        .whenStarting(mob -> {
                            if (isAxeWielder()) {
                                this.triggerAnim("attackController", "attack");
                            }
                            else {
                                this.triggerAnim("attackController", RandomUtil.getRandomSelection(animations));
                            }
                        })
        );
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "livingController", 5, event -> {
            if (event.isMoving() && !swinging) {
                return event.setAndContinue(DefaultElderiaAnimations.WALK);
            }
            return event.setAndContinue(DefaultElderiaAnimations.IDLE);
        })).add(new AnimationController<>(this, "attackController", 5, event -> {
            swinging = false;
            return PlayState.STOP;
        }).triggerableAnim("attack", DefaultElderiaAnimations.ATTACK)
                .triggerableAnim("claw_attack_right", CLAW_ATTACK_RIGHT)
                .triggerableAnim("claw_attack_left", CLAW_ATTACK_LEFT)
                .triggerableAnim("claw_attack_double", CLAW_ATTACK_DOUBLE));
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        if (isAxeWielder())
            return super.isWithinMeleeAttackRange(entity);
        var attackBox = this.getBoundingBox().inflate(1.5);
        return attackBox.intersects(entity.getBoundingBox());
    }
}
