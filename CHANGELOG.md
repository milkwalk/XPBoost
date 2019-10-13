# Change Log

## [3.2.1] - 2/10/2019

### Fixed
- bug with `PlayerInteractEvent` for 1.8.8

## [3.2.0] - 2/10/2019

### Added
- Vietnamese language (VN) thanks to @fecxica
- MyPet support
- command `/xpb giveDefinedBoost <player/all> <boostId> [durationInSeconds]` - gives a player (or everyone) some predefined boost _(boost does not have to be enabled in the config)_
- ability to choose where you want your messages to appear in the `CHAT` or `ACTIONBAR` for `experienceGainedMessagesOptions` and `activeBoostReminderOptions` settings

### Changed

- renamed some configuration keys for better readability **(you may want to regenerate your config file)**

## [3.1.0] - 3/06/2019

### Added
- support for Minecraft 1.14.2

### Changed
- inventory recognition using `InventoryHolder`

## [3.0.2] - 3/02/2019

### Changed

- Updated to the latest mcmmo (v2.1.7) version

### Fixed

- Fixed bug with xpboost item on 1.8 servers

## [3.0.1] - 18/01/2019

### Removed

- Direct placeholderapi dependency (use xpboost expansion for placeholders)

### Fixed

- Interaction event for < 1.9 version

## [3.0.0] - 6/01/2019

### Added
- updated to the 1.13.2 Spigot
- `%timeleft%` placeholder to the actionbar message
- simplified Chinese (ZHS) language (thanks f0rb1d)
- `boostEndSound` option to config under `settings`
- maven

### Changed
- refactored large portion of the plugin
- minor improvements
- use of NMS for actionbar messages

### Removed
- factions support

## [2.0.6] - 5/01/2019

Initial verstion