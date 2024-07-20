package net.pixeldreamstudios.beings_of_elderia.entity.demons;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.AbstractDemonEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.constant.DefaultElderiaAnimations;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.util.RandomUtil;

public class ImpEntity extends AbstractDemonEntity {
    private static final String[] animations = {"claw_attack_right", "claw_attack_left", "claw_attack_double"};
    public static final RawAnimation CLAW_ATTACK_RIGHT = RawAnimation.begin().then(animations[0], Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation CLAW_ATTACK_LEFT = RawAnimation.begin().then(animations[1], Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation CLAW_ATTACK_DOUBLE = RawAnimation.begin().then(animations[2], Animation.LoopType.PLAY_ONCE);

    public ImpEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.navigation = new SmoothGroundNavigation(this, level());
        if (RandomUtil.fiftyFifty()) {
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_AXE));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, BeingsOfElderia.config.impHealth)
                .add(Attributes.ATTACK_DAMAGE, BeingsOfElderia.config.impAttackDamage)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.FOLLOW_RANGE, 64);
    }

    @Override
    public BrainActivityGroup<AbstractDemonEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((target, entity) -> !target.isAlive() || !entity.hasLineOfSight(target)),
                new SetWalkTargetToAttackTarget<>()
                        .speedMod((mob, livingEntity) -> isHoldingWeapon() ? 1.2f : 1.5f),
                new AnimatableMeleeAttack<>(12)
                        .attackInterval(mob -> isHoldingWeapon() ? 40 : 20)
                        .whenStarting(mob -> {
                            if (isHoldingWeapon()) {
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
        controllerRegistrar.add(new AnimationController<>(this, "livingController", 3, event -> {
            if (event.isMoving() && !swinging) {
                return event.setAndContinue(DefaultElderiaAnimations.WALK);
            }
            return event.setAndContinue(DefaultElderiaAnimations.IDLE);
        })).add(new AnimationController<>(this, "attackController", 3, event -> {
            swinging = false;
            return PlayState.STOP;
        }).triggerableAnim("attack", DefaultElderiaAnimations.ATTACK)
                .triggerableAnim("claw_attack_right", CLAW_ATTACK_RIGHT)
                .triggerableAnim("claw_attack_left", CLAW_ATTACK_LEFT)
                .triggerableAnim("claw_attack_double", CLAW_ATTACK_DOUBLE));
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        if (isHoldingWeapon())
            return super.isWithinMeleeAttackRange(entity);
        var attackBox = this.getBoundingBox().inflate(1.5);
        return attackBox.intersects(entity.getBoundingBox());
    }

    private boolean isHoldingWeapon() {
        return !getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
    }
}
