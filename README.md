# msgDonutSMP

DonutSMP-style private messaging plugin for Paper servers.

Features:

- Private messaging `/msg` and `/r`
- Persistent ignore lists with `/msgignore` and `/msgunignore`
- Ignore GUI `/msgignorelist` showing player heads
- Vanish protection via Paper visibility checks with PlaceholderAPI fallbacks (`%essentials_vanished%`, `%supervanish_is_vanished%`)
- Configurable MiniMessage formats and messages
- bStats support
- PlaceholderAPI placeholders: `%msgdonut_last_partner%`, `%msgdonut_ignored_count%`, `%msgdonut_ignored_list%`

## Build

Build with Maven and drop the resulting JAR into your Paper server `plugins/` folder.

## Commands

- `/msg <player> <message>` — Send a private message.
- `/r <message>` — Reply to the last person you messaged or who messaged you.
- `/msgignore <player>` — Add a player to your personal ignore list.
- `/msgunignore <player>` — Remove a player from your ignore list.
- `/msgignorelist` — Open a GUI listing ignored players (right-click a head to unignore).
- `/msgreload` — Reload plugin config and data (requires `msg.admin`).

## Permissions

- `msg.use` — Access to `/msg`, `/r`, `/msgignore`, `/msgignorelist`.
- `msg.bypass.vanish` — Allows staff to message vanished players.
- `msg.admin` — Permission for `/msgreload`.
- `msg.bypass.ignore` — Staff with this permission cannot be ignored (configurable).

## Configuration

All messages and formats are configurable in `config.yml` using MiniMessage/Hex.

Important placeholders for formats:

- `%player%` — the other player's name
- `%message%` — the message text

Example formats in `config.yml`:

- `formats.sent`: `[Me -> %player%] %message%`
- `formats.receive`: `[%player% -> Me] %message%`

## PlaceholderAPI

If PlaceholderAPI is installed, the plugin registers placeholders:

- `%msgdonut_last_partner%` — last conversation partner's name (or `none`).
- `%msgdonut_ignored_count%` — number of players you currently ignore.
- `%msgdonut_ignored_list%` — comma-separated list of ignored player names.

If `placeholderapi.differentiate-vanished` is enabled in `config.yml`, any placeholder output that resolves a player name appends `placeholderapi.vanished-suffix` when that player is vanished to the viewer.

## Building & Testing

From the project root:

```bash
mvn clean package
```

Copy the generated JAR from `target/` into your Paper server `plugins/` folder, and ensure `PlaceholderAPI` (optional) and bStats are present on the server.
