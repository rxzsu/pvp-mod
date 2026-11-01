package pvp.mod.client.modules

object Modules {
    lateinit var noFovChange: Module
    lateinit var noNausea: Module
    lateinit var noHurtTilt: Module
    lateinit var noFireOverlay: Module
    lateinit var noWeather: Module
    lateinit var noViewBobbing: Module
    lateinit var fullbright: Module
    lateinit var toggleSprint: Module
    lateinit var hud: Module
    lateinit var keystrokes: Module
    lateinit var noPumpkin: Module
    lateinit var noVignette: Module
    lateinit var statusHud: Module

    fun init() {
        noFovChange   = SimpleModule("noFovChange",   "No FOV Change",   default = true,  column = Module.Column.VISUAL)
        noNausea      = SimpleModule("noNausea",      "No Nausea",       default = true,  column = Module.Column.VISUAL)
        noHurtTilt    = SimpleModule("noHurtTilt",    "No Hurt Tilt",    default = true,  column = Module.Column.VISUAL)
        noFireOverlay = SimpleModule("noFireOverlay", "No Fire Overlay", default = true,  column = Module.Column.VISUAL)
        noWeather     = SimpleModule("noWeather",     "No Weather",      default = true,  column = Module.Column.VISUAL)
        noViewBobbing = SimpleModule("noViewBobbing", "No View Bobbing", default = true,  column = Module.Column.VISUAL)
        noPumpkin     = SimpleModule("noPumpkin",     "No Pumpkin",      default = false, column = Module.Column.VISUAL)
        noVignette    = SimpleModule("noVignette",    "No Vignette",     default = false, column = Module.Column.VISUAL)
        fullbright    = SimpleModule("fullbright",    "Fullbright",      default = false, column = Module.Column.UTILITY)
        toggleSprint  = SimpleModule("toggleSprint",  "Toggle Sprint",   default = false, column = Module.Column.UTILITY)
        hud           = SimpleModule("hud",           "HUD",             default = true,  column = Module.Column.UTILITY)
        keystrokes    = SimpleModule("keystrokes",    "Keystrokes",      default = false, column = Module.Column.UTILITY)
        statusHud     = SimpleModule("statusHud",     "Effects & Armor", default = true,  column = Module.Column.UTILITY)

        ModuleManager.register(noFovChange)
        ModuleManager.register(noNausea)
        ModuleManager.register(noHurtTilt)
        ModuleManager.register(noFireOverlay)
        ModuleManager.register(noWeather)
        ModuleManager.register(noViewBobbing)
        ModuleManager.register(fullbright)
        ModuleManager.register(toggleSprint)
        ModuleManager.register(hud)
        ModuleManager.register(keystrokes)
        ModuleManager.register(noPumpkin)
        ModuleManager.register(noVignette)
        ModuleManager.register(statusHud)
    }

    private class SimpleModule(id: String, name: String, default: Boolean, column: Module.Column) :
        Module(id, name, default, column)
}
