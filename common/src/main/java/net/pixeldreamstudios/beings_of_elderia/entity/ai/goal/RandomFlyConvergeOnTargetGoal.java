package net.pixeldreamstudios.beings_of_elderia.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.pixeldreamstudios.beings_of_elderia.entity.demons.DemonEntity;

import java.util.EnumSet;

public class RandomFlyConvergeOnTargetGoal extends Goal {
    private final DemonEntity parentEntity;
    private LivingEntity target;

    public RandomFlyConvergeOnTargetGoal(DemonEntity entity) {
        this.parentEntity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        MoveControl movementController = this.parentEntity.getMoveControl();
        target = this.parentEntity.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        return !movementController.hasWanted() || this.parentEntity.distanceToSqr(target) > 2.0D;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && this.parentEntity.distanceToSqr(target) > 2.0D;
    }

    @Override
    public void start() {
        if (target != null) {
            moveTowardsTarget();
        }
    }

    @Override
    public void tick() {
        if (target != null && target.isAlive()) {
            moveTowardsTarget();
            rotateTowardsTarget();
        }
    }

    private void moveTowardsTarget() {
        double d0 = target.getX();
        double d1 = target.getY() + target.getEyeHeight();
        double d2 = target.getZ();

        this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 1.5);
    }

    private void rotateTowardsTarget() {
        double dx = target.getX() - this.parentEntity.getX();
        double dz = target.getZ() - this.parentEntity.getZ();

        float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0F / Math.PI)) - 90.0F;
        this.parentEntity.setYRot(this.rotlerp(this.parentEntity.getYRot(), targetYaw, 10.0F));
        this.parentEntity.yHeadRot = this.parentEntity.getYRot(); // Sync head rotation with body
    }

    private float rotlerp(float current, float target, float maxChange) {
        float delta = Mth.wrapDegrees(target - current);
        return current + Mth.clamp(delta, -maxChange, maxChange);
    }
}
