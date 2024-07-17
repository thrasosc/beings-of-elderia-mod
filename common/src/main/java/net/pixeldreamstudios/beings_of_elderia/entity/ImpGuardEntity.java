package net.pixeldreamstudios.beings_of_elderia.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.pixeldreamstudios.beings_of_elderia.entity.abstraction.AbstractDemonEntity;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;

public class ImpGuardEntity extends AbstractDemonEntity {
    public ImpGuardEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.navigation = new SmoothGroundNavigation(this, level());
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((target, entity) -> !target.isAlive() || !entity.hasLineOfSight(target)),
                new SetWalkTargetToAttackTarget<>().speedMod((mob, livingEntity) -> 1.5f),
                new AnimatableMeleeAttack<>(6)
                        .whenStarting(mob -> {
                            this.triggerAnim("attackController", "attack");
                        })
        );
    }
}
