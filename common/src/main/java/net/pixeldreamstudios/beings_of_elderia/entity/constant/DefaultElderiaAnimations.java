package net.pixeldreamstudios.beings_of_elderia.entity.constant;

import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.RawAnimation;

public final class DefaultElderiaAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation FLY = RawAnimation.begin().thenLoop("fly");
    public static final RawAnimation ATTACK = RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE);
}
