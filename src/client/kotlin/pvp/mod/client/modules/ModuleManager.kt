package pvp.mod.client.modules

import pvp.mod.PvpMod
import pvp.mod.client.config.Config

object ModuleManager {
    private val modules = linkedMapOf<String, Module>()

    fun register(module: Module) {
        require(module.id !in modules) { "Duplicate module id: ${module.id}" }
        modules[module.id] = module
    }

    fun all(): Collection<Module> = modules.values

    fun byId(id: String): Module? = modules[id]

    fun column(column: Module.Column): List<Module> = modules.values.filter { it.guiColumn == column }

    /** Применяет состояния из конфига (после Config.load()). */
    fun applyConfig() {
        for (m in modules.values) {
            Config.get().modules[m.id]?.let { m.enabled = it }
        }
        PvpMod.LOGGER.info("Loaded ${modules.size} modules")
    }
}
