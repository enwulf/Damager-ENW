package ru.enwulf.damagerenw.animations

import org.bukkit.entity.TextDisplay
import ru.enwulf.damagerenw.animations.impl.ScaleAnimation
import ru.enwulf.damagerenw.animations.impl.ScaleAnimation.AnimationType
import ru.enwulf.damagerenw.animations.impl.TeleportAnimation
import ru.enwulf.damagerenw.config.Config

object AnimationManager {

    fun TextDisplay.animate() {
        if (Config.ANIMATIONS_INCREASE_ENABLED) {
            playAnimation(ScaleAnimation(this, AnimationType.INCREASE), Config.ANIMATIONS_INCREASE_DELAY)
        }

        if (Config.ANIMATIONS_DECREASE_ENABLED) {
            playAnimation(ScaleAnimation(this, AnimationType.DECREASE), Config.ANIMATIONS_DECREASE_DELAY)
        }

        if (Config.ANIMATIONS_UPWARD_ENABLED) {
            playAnimation(TeleportAnimation(this, Config.ANIMATIONS_UPWARD_SPEED, Config.ANIMATIONS_UPWARD_MAX_HEIGHT != -1), 0L)
        }
    }

    private fun playAnimation(animation: Animation, delay: Long) {
        animation.play(delay)
    }
}