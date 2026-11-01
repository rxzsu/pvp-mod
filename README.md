# PvP Mod

Клиентский PvP-ультиматив для Fabric: Click GUI по Right Shift, Keystrokes HUD, Fullbright, Toggle Sprint, Zoom, отключение мешающих эффектов и HUD с координатами/FPS/пингом. Все фичи — модули, полностью настраиваются.

## Возможности

### Click GUI (Right Shift)
Все модули в одном меню: включаются мышкой, состояние сохраняется сразу. Доступен также через ModMenu → PvP Mod → Configure.

### Keystrokes HUD
WASD, пробел и CPS мыши (ЛКМ/ПКМ) в углу экрана — как в Lunar/Badlion.

### Модули
- 💡 **Fullbright** — яркостный буст (PageUp/PageDown — ручная гамма)
- 🏃 **Toggle Sprint** — автоспринт одним нажатием
- 🔍 **Zoom** — плавный зум (сила = `zoomSensitivity` в конфиге)
- 🚫 **No FOV Change** / 🤢 **No Nausea** / 💥 **No Hurt Tilt**
- 🔥 **No Fire Overlay** (вода не трогается) / 🌧️ **No Weather** / 🚶 **No View Bobbing**
- 📊 **HUD** — координаты, FPS и пинг
- 🌫️ **Blur в Click GUI** — размытие мира позади меню, включая собственный gaussian-шейдер (`menuBlur`/`fancyBlur` в конфиге)
- ⌨️ **Keystrokes** — см. выше

## Хоткеи (по умолчанию)

| Клавиша | Действие |
|---|---|
| `Right Shift` | Открыть Click GUI |
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
  "keystrokesOffsetY": 6,
  "menuBlur": true,
  "fancyBlur": true
}
```

## Сборка

```bash
./gradlew build
```

## Лицензия

MIT
