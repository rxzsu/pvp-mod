package pvp.mod.client.handler

object FullbrightHandler {
    var enabled = false

    fun loadFromConfig(value: Boolean) {
        enabled = value
    }

    fun toggle() {
        enabled = !enabled
    }
}
