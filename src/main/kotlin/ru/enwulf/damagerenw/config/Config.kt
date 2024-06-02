package ru.enwulf.damagerenw.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.Configuration

object Config {
    private lateinit var config: Configuration

    // Global Settings
    private const val GLOBAL_SETTINGS = "global-settings"
    val SHOW_ONLY_PLAYER_DAMAGE: Boolean get() = config.getBoolean("$GLOBAL_SETTINGS.show-only-player-damage")
    val DAMAGE_TEXT_LOCATION: String? get() = config.getString("$GLOBAL_SETTINGS.text-location")
    val DISPLAY_SHOW_THROUGH_WALLS: Boolean get() = config.getBoolean("$GLOBAL_SETTINGS.display.show-through-walls")
    val DISPLAY_SCALE: Double get() = config.getDouble("$GLOBAL_SETTINGS.display.scale")
    val DISPLAY_SHADOWED: Boolean get() = config.getBoolean("$GLOBAL_SETTINGS.display.shadowed")
    val DISPLAY_OPACITY: Byte get() = config.getInt("$GLOBAL_SETTINGS.display.opacity").toByte()
    val DISPLAY_DURATION: Long get() = config.getLong("$GLOBAL_SETTINGS.display.duration")
    val ANIMATIONS_INCREASE_ENABLED: Boolean get() = config.getBoolean("$GLOBAL_SETTINGS.animations.increase.enabled")
    val ANIMATIONS_INCREASE_DELAY: Long get() = config.getLong("$GLOBAL_SETTINGS.animations.increase.delay")
    val ANIMATIONS_DECREASE_ENABLED: Boolean get() = config.getBoolean("$GLOBAL_SETTINGS.animations.decrease.enabled")
    val ANIMATIONS_DECREASE_DELAY: Long get() = config.getLong("$GLOBAL_SETTINGS.animations.decrease.delay")
    val ANIMATIONS_UPWARD_ENABLED: Boolean get() = config.getBoolean("$GLOBAL_SETTINGS.animations.upward.enabled")
    val ANIMATIONS_UPWARD_MAX_HEIGHT: Int get() = config.getInt("$GLOBAL_SETTINGS.animations.upward.max-height")
    val ANIMATIONS_UPWARD_SPEED: Double get() = config.getDouble("$GLOBAL_SETTINGS.animations.upward.speed")



    // Damage-Display Settings
    private const val DAMAGE_DISPLAY = "damage-display"
    val DAMAGE_TEXT: String? get() = config.getString("$DAMAGE_DISPLAY.damage-text")
    val PROJECTILE_DAMAGE_IGNORE: List<String> get() = config.getStringList("$DAMAGE_DISPLAY.projectile-damage-ignore")
    val IGNORE_ZERO_DAMAGE: Boolean get() = config.getBoolean("$DAMAGE_DISPLAY.ignore-zero-damage")

    // Kill-Display Settings
    private const val KILL_DISPLAY = "kill-display"
    val KILL_TEXT_ENABLED: Boolean get() = config.getBoolean("$KILL_DISPLAY.enabled")
    val KILL_TEXT_TYPE: String? get() = config.getString("$KILL_DISPLAY.type")
    val KILL_TEXT_SEQUENTIAL_RESET: Long get() = config.getLong("$KILL_DISPLAY.sequential-reset")
    val KILL_TEXT_MESSAGES: List<String> get() = config.getStringList("$KILL_DISPLAY.messages")
    val SHOW_DAMAGE_ON_KILL: Boolean get() = config.getBoolean("$KILL_DISPLAY.show-damage-on-kill")


    val TOTAL_DISPLAY_DURATION: Long get() = DISPLAY_DURATION +
                (if (ANIMATIONS_INCREASE_ENABLED) ANIMATIONS_INCREASE_DELAY else 0) +
                (if (ANIMATIONS_DECREASE_ENABLED) ANIMATIONS_DECREASE_DELAY else 0)


    fun init(configuration: Configuration) {
        config = configuration
    }


    fun getFormattedText(template: String?, replacements: Map<String, String>): Component {
        var parsedTemplate = template ?: ""
        replacements.forEach { (key, value) ->
            parsedTemplate = parsedTemplate.replace("%$key%", value)
        }
        val mm = MiniMessage.miniMessage()
        return mm.deserialize(parsedTemplate)
    }
}