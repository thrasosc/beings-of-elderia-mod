package net.pixeldreamstudios.beings_of_elderia.entity.demons;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.AbstractDemonEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.ai.DemonFloatControl;
import net.pixeldreamstudios.beings_of_elderia.entity.ai.goal.RandomFlyConvergeOnTargetGoal;
import net.pixeldreamstudios.beings_of_elderia.entity.constant.DefaultElderiaAnimations;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StayWithinDistanceOfAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DemonEntity extends AbstractDemonEntity {
    public DemonEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        moveControl = new DemonFloatControl(this);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, BeingsOfElderia.config.demonHealth)
                .add(Attributes.ATTACK_DAMAGE, BeingsOfElderia.config.demonAttackDamage)
                .add(Attributes.MOVEMENT_SPEED, 0.55)
                .add(Attributes.FLYING_SPEED, 0.75)
                .add(Attributes.FOLLOW_RANGE, 64);
    }

    @Override
    public void knockback(double x, double y, double z) {
        super.knockback(0, 0, 0);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "livingController", 5, event -> {
            if (!onGround()) {
                return event.setAndContinue(DefaultElderiaAnimations.FLY);
            }
            return event.setAndContinue(DefaultElderiaAnimations.FLY);
        })).add(new AnimationController<>(this, "attackController", 5, event -> {
            swinging = false;
            return PlayState.STOP;
        }).triggerableAnim("attack", DefaultElderiaAnimations.ATTACK));
    }

    @Override
    public List<ExtendedSensor<AbstractDemonEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<AbstractDemonEntity>()
//                        .setRadius()
                        .setPredicate((target, entity) -> target instanceof Player),
                new HurtBySensor<>(),
                new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new LookAtTarget(),
                new StayWithinDistanceOfAttackTarget<>().speedMod(2.25F),
                new MoveToWalkTarget<>()
        );
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
    public BrainActivityGroup<AbstractDemonEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((target, entity) -> !target.isAlive() || !entity.hasLineOfSight(target)),
                new SetWalkTargetToAttackTarget<>().speedMod((mob, livingEntity) -> 2.05f),
                new AnimatableMeleeAttack<>(9)
                        .whenStarting(mob -> {
                            this.triggerAnim("attackController", "attack");
                        })
//                new ReactToUnreachableTarget<>()
        );
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        final var flyingpathnavigator = new FlyingPathNavigation(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(true);
        flyingpathnavigator.setCanFloat(true);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        if (isInWater()) {
            moveRelative(0.02F, movementInput);
            move(MoverType.SELF, getDeltaMovement());
            this.setDeltaMovement(getDeltaMovement().scale(0.8F));
        } else if (isInLava()) {
            moveRelative(0.02F, movementInput);
            move(MoverType.SELF, getDeltaMovement());
            this.setDeltaMovement(getDeltaMovement().scale(0.5D));
        } else {
            final var ground = BlockPos.containing(this.getX(), this.getY() - 1.0D, this.getZ());
            var f = 0.91F;
            if (onGround()) f = level().getBlockState(ground).getBlock().getFriction() * 0.91F;
            final var f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (onGround()) f = level().getBlockState(ground).getBlock().getFriction() * 0.91F;
            moveRelative(onGround() ? 0.1F * f1 : 0.02F, movementInput);
            move(MoverType.SELF, getDeltaMovement());
            this.setDeltaMovement(getDeltaMovement().scale(f));
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(5, new RandomFlyConvergeOnTargetGoal(this));
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {

    }
}
