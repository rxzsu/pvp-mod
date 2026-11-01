# PvP Mod

Клиентский PvP-ультиматив для Fabric: Keystrokes HUD, Fullbright, Toggle Sprint, Zoom, отключение мешающих эффектов и HUD с координатами/FPS/пингом. Все фичи настраиваются через ModMenu.

## Возможности

### Настройки через ModMenu
ModMenu → PvP Mod → Configure: тогглы всех модулей + переназначение клавиш (ModMenu ставить не обязательно, конфиг есть и в `config/pvp-mod.json`).

### Keystrokes HUD
WASD, пробел и CPS мыши (ЛКМ/ПКМ) в углу экрана — как в Lunar/Badlion.

### Модули
- 💡 **Fullbright** — яркостный буст (PageUp/PageDown — ручная гамма)
- 🏃 **Toggle Sprint** — автоспринт одним нажатием
- 🔍 **Zoom** — плавный зум (сила = `zoomSensitivity` в конфиге)
- 🚫 **No FOV Change** / 🤢 **No Nausea** / 💥 **No Hurt Tilt**
- 🔥 **No Fire Overlay** (вода не трогается) / 🌧️ **No Weather** / 🚶 **No View Bobbing**
- 📊 **HUD** — координаты + направление (N/E/S/W), FPS и пинг с цветовой индикацией, скорость (m/s)
- 🧪 **Effects & Armor** — таймеры активных зелий и прочность брони (цвет от зелёного к красному)
- 🎃 **No Pumpkin** — тыква не закрывает экран
- 🌑 **No Vignette** — виньетка при уроне отключена
- ⌨️ **Keystrokes** — см. выше

## Хоткеи (по умолчанию)

| Клавиша | Действие |
|---|---|
| `V` | Zoom (зажимать) |
| `R` / `F` / `B` / `H` / `N` / `C` / `O` / `K` / `J` | Тогглы модулей |
| `Page Up` / `Page Down` | Яркость +/− |

Все клавиши переназначаются в настройках Minecraft.

## Установка

1. Скачай `.jar` из [релизов](https://github.com/rxzsu/pvp-mod/releases) или с [Modrinth](https://modrinth.com/mod/pvp-utility-mod)
2. Кинуть в `.minecraft/mods/`
3. Запустить игру

## Требования

- Minecraft 26.2
- Fabric Loader 0.19+
- Fabric API
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)

## Конфиг

`config/pvp-mod.json` (2.0+; конфиг от 1.x мигрирует автоматически):

```json
{
  "modules": {
    "noFovChange": true,
    "noNausea": true,
    "noHurtTilt": true,
    "noFireOverlay": true,
    "noWeather": true,
    "noViewBobbing": true,
    "fullbright": false,
    "toggleSprint": false,
    "hud": true,
    "keystrokes": false
  },
  "zoomSensitivity": 0.2,
  "keystrokesOffsetX": 6,
  "keystrokesOffsetY": 6
}
```

## Сборка

```bash
./gradlew build
```

## Лицензия

MIT
