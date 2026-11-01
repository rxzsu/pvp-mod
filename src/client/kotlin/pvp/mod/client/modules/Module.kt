package pvp.mod.client.modules

import net.minecraft.client.KeyMapping
import pvp.mod.client.config.Config

/**
 * Модуль-фича. Состояние хранится в Config и сохраняется при каждом переключении.
 */
abstract class Module(
    val id: String,
    val name: String,
    var enabled: Boolean,
    val guiColumn: Column = Column.UTILITY
) {
    var keybind: KeyMapping? = null

    fun toggle() {
        enabled = !enabled
        Config.save()
    }

    enum class Column { VISUAL, UTILITY }
}
