# PrizeCommands

## Install
Simply download the PrizeCommands Jar from GitHub Releases or MBedwars.com, and drop it in your MBedwars Addons folder. 
Once you restart, your config file will automatically be generated.

## Setup
### Create a Prize
You can create unlimited prizes, and even reuse them on different events!
You can add a prize to your config like this:
```yml
  Prize-ID:
    Enabled: true
    Permission: ''
    Commands:
      - ''
    Broadcast:
      - ''
    Player-Message:
      - ''
    Supported-Arenas:
      - ''
```

---
`Prize-ID` The Identifier of this prize. This can be anything (As long as it follows the YML formatting rules). 
Using the identifier you can set this prize to run on certain events.

---
`Enabled` This config simply allows you to disable a prize without having to delete it from the config.

---
`Permission` Set required permission for a prize to be run. Leave empty or remove config to not require a permission.

---
`Commands` Command(s) that get executed as console when the prize is triggered.

---
`Broadcast` Message(s) broadcasted to every player in an arena when a prize is triggered.

---
`Player-Message` Message(s) sent to player who earned the specifed prize.

---
`Supported-Arenas` Arena(s) in which a prizes are active. Leave blank or remove config to support ALL arenas. The config SUPPORTS [Arena Picker Conditions](https://wiki.mbedwars.com/en/Features/Arena-Picker)

### Add Placeholders into prize Commands/Private-Messages/Broadcasts
#### Placeholders supported by ALL Prize Events
`{team-name}, {team-color}, {team-color-code}, {arena-name}, {arena-world}, {player-real-name}, {player-display-name}, {player-x}, {player-y}, {player-z}`
#### Additional Placeholders supported by Win/Lose Prizes
`{winner-team-name}, {winner-team-color}, {winner-team-color-code}`
#### Additional Placeholders supported by Bed Break Prizes
`{destroyed-team-name}, {destroyed-team-color}, {destroyed-team-color-code}`
#### Additional Placeholders supported by Kill/Final Kill Prizes
`{killer-team-name}, {killer-team-color}, {killer-team-color-code}, {Killer-real-name}, {Killer-display-name}, {killer-x}, {killer-y}, {killer-z}`

## Set a prize to trigger
After you create a prize, you need to add it to event. Let's say we want to give someone a prize when they break a bed. 
Simply add that prize to the Player Break Bed prize list!
```yml
Bed-Break-Prizes:
  - ''
```
You can add as many prizes as you want to the events!

Still need help? Join our [Discord.](https://discord.gg/3mJuxUW) Please be a verified MBedwars buyer, or buyer helper before asking for help on MetallicGoat's Discord server.
