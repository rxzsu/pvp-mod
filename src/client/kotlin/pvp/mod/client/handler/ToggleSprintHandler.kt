package pvp.mod.client.handler

object ToggleSprintHandler {
    var enabled = false

    fun loadFromConfig(value: Boolean) {
        enabled = value
    }

    fun toggle() {
        enabled = !enabled
    }
}
