package net.pixeldreamstudios.beings_of_elderia.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.pixeldreamstudios.beings_of_elderia.entity.abstraction.AbstractDemonEntity;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import org.jetbrains.annotations.NotNull;

public class DemonEntity extends AbstractDemonEntity {
    public DemonEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.navigation = new FlyingPathNavigation(this, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((target, entity) -> !target.isAlive() || !entity.hasLineOfSight(target)),
                new SetWalkTargetToAttackTarget<>().speedMod((mob, livingEntity) -> 1.5f),
                new AnimatableMeleeAttack<>(9)
                        .whenStarting(mob -> {
                            this.triggerAnim("attackController", "attack");
                        })
        );
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        final var flyingpathnavigator = new FlyingPathNavigation(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(true);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {

    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<AbstractDemonEntity>(
                        new TargetOrRetaliate<>().alertAlliesWhen((mob, entity) -> this.isAggressive()),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomFlyingTarget<>().speedModifier(1.2f),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }
}
