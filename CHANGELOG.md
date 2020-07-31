# Change Log

## [3.4.1] - 1/08/2020

### Fixed
- rounding issue with experince for jobs
- 1.15.2 action bar messages

## [3.4.0] - 10/07/2020

### Added
- Minecraft 1.16.1 support

### Fixed
- mysql issue with not valid connections
- other minor fixes

## [3.3.0] - 29/11/2019

### Added
- set `api-version` to `1.13` in `plugin.yml`
- ability to disable effect of xp boost for experience from _XP Bottles_ for **Paper Spigot**

### Fixed

- colorization for `ACTIONBAR` messages
- `/xpb give` command dsiabling all boost types if none were provided (should be enabled by default)

## [3.2.2] - 18/10/2019

### Added
- lang strings for `/xpboost info` command
- lang string for non existent command

### Fixed
- issue with start up on paper spigot

## [3.2.1] - 13/10/2019

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