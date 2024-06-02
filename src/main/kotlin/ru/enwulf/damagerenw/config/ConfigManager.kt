package ru.enwulf.damagerenw.config

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class ConfigurationFile(
    plugin: JavaPlugin,
    name: String,
    overwrite: Boolean = false
) {
    private val file: File = File(plugin.dataFolder, "$name.yml")
    private val configuration: YamlConfiguration = YamlConfiguration()

    init {
        if (!file.exists() || overwrite) {
            plugin.saveResource("$name.yml", overwrite)
        }
        load()
        Config.init(configuration)
    }

    private fun load() {
        try {
            configuration.load(file)
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reload() {
        load()
        Config.init(configuration)
    }

    fun save() {
        try {
            configuration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}