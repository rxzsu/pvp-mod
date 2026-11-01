# PvP Mod

Клиентский PvP-ультиматив для Fabric: Fullbright, Toggle Sprint, Zoom, отключение мешающих эффектов, HUD с координатами/FPS/пингом и экран настроек. Полностью настраивается — каждая фича переключается хоткеем или через ModMenu.

## Возможности

- 💡 **Fullbright** — полный яркостный буст независимо от настроек гаммы, с плавной регулировкой (PageUp/PageDown)
- 🏃 **Toggle Sprint** — автоспринт одним нажатием
- 🔍 **Zoom** — плавный зум (как в OptiFine)
- 🚫 **No FOV Change** — убирает растяжение экрана от зелий скорости и замедления
- 🤢 **No Nausea** — убирает эффект тошноты/портала
- 💥 **No Hurt Tilt** — убирает тряску экрана при получении урона
- 🔥 **No Fire Overlay** — убирает огонь, закрывающий экран
- 🌧️ **No Weather** — убирает дождь и грозу
- 🚶 **No View Bobbing** — убирает покачивание камеры
- 📊 **HUD** — координаты, FPS и пинг в углу экрана

## Хоткеи (по умолчанию)

| Клавиша | Действие |
|---|---|
| `V` | Zoom (зажимать) |
| `R` | Toggle Sprint |
| `F` | Fullbright |
| `Page Up` / `Page Down` | Яркость +/− |
| `B` | No View Bobbing |
| `H` | No Hurt Tilt |
| `N` | No Nausea |
| `C` | No FOV Change |
| `O` | No Fire Overlay |
| `K` | No Weather |
| `J` | HUD |

Все клавиши переназначаются в настройках Minecraft или в экране настроек мода.

## Установка

1. Скачай `.jar` из [релизов](https://github.com/FUXKVOB/pvp-mod/releases) или с [Modrinth](https://modrinth.com/mod/pvp-utility-mod)
2. Кинуть в `.minecraft/mods/`
3. Запустить игру

## Требования

- Minecraft 26.2
- Fabric Loader 0.14+
- Fabric API
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)

## Конфиг и настройки

Экран настроек открывается через **ModMenu → PvP Mod → Configure** (ModMenu ставить не обязательно).

После первого запуска появляется `config/pvp-mod.json` — все фичи можно настроить и руками:

```json
{
  "fullbright": false,
  "toggleSprint": false,
  "noViewBobbing": true,
  "noHurtTilt": true,
  "noNausea": true,
  "noFovChange": true,
  "noFireOverlay": true,
  "noWeather": true,
  "hudEnabled": true,
  "zoomSensitivity": 0.2
}
```

Поставь `false` любому параметру, чтобы выключить фичу.

## Сборка

```bash
./gradlew build
```

## Лицензия

MIT
