# msgDonutSMP

A highly-configurable DonutSMP-style private messaging system for Paper servers (Java 17). Features vanish-aware messaging, persistent ignore lists with GUI management, MiniMessage formatting, PlaceholderAPI placeholders, and optional bStats metrics.

## Features

- Full message flow: `/msg`, `/r`, `/msgignore`, `/msgunignore`, `/msgignorelist`, `/msgreload`.
- Vanish protection that checks Paper visibility and supports PlaceholderAPI vanish tags (OR gate).
- Persistent ignore storage with a head-based GUI list for quick unignore actions.
- MiniMessage-powered configurable message and format output.
- PlaceholderAPI expansion for last partner and ignore stats.
- Optional bStats metrics collection for plugin usage insights.

## Commands & Permissions

- `msg.use` — Use messaging and ignore commands (default: true)
- `msg.bypass.vanish` — Allow messaging vanished players (default: op)
- `msg.bypass.ignore` — Exempt player from being ignored (default: op)
- `msg.admin` — Reload config/data and admin actions (default: op)

Example commands:

- `/msg <player> <message>` — Send a private message.
- `/r <message>` — Reply to your last conversation partner.
- `/msgignore <player>` — Add a player to your ignore list.
- `/msgunignore <player>` — Remove a player from your ignore list.
- `/msgignorelist` — Open ignore list GUI.
- `/msgreload` — Reload `config.yml` and data (requires `msg.admin`).

## Config (high level)

The plugin ships with `config.yml` exposing:

- `options.allow-ignore-staff` — Whether staff can be ignored.
- `placeholderapi.differentiate-vanished` — Append vanished suffix in placeholders.
- `placeholderapi.vanished-suffix` — Suffix appended for vanished players.
- `placeholderapi.vanish-placeholders` — PlaceholderAPI vanish tags (OR logic).
- `formats.sent` / `formats.receive` — Message format templates.
- `messages.*` — All user-facing text (MiniMessage-compatible).
- `gui.title` — Ignore list GUI title.

Use MiniMessage tags and hex colors in format/message entries. PlaceholderAPI placeholders are used when PlaceholderAPI is present.

## Notes for Server Owners

- bStats ID is hardcoded to `31310` in the plugin main class.
- Vanish detection supports both Paper visibility and configured PlaceholderAPI tags.
- Ignore data is persisted by the plugin and reloaded on startup.

## Technical

- Java: 17+
- Target: Paper (1.20.2+)
- Optional: PlaceholderAPI (softdepend)
