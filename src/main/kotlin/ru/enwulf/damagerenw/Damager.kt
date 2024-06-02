package ru.enwulf.damagerenw

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import ru.enwulf.damagerenw.commands.DamagerCommand
import ru.enwulf.damagerenw.config.ConfigurationFile
import ru.enwulf.damagerenw.listeners.DamageListener

lateinit var app: Damager

class Damager : JavaPlugin() {
    val configFile = ConfigurationFile(this, "config")

    override fun onEnable() {
        app = this
        Bukkit.getPluginManager().registerEvents(DamageListener(), this)

        DamagerCommand(this)

        configFile.save()
    }
}