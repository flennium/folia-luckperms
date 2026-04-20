![](https://raw.githubusercontent.com/LuckPerms/branding/master/banner/banner.png "Banner")
# LuckPerms — Folia Fork
[![Build Status](https://ci.lucko.me/job/LuckPerms/badge/icon)](https://ci.lucko.me/job/LuckPerms/)
[![javadoc](https://javadoc.io/badge2/net.luckperms/api/javadoc.svg)](https://javadoc.io/doc/net.luckperms/api)
[![Maven Central](https://img.shields.io/maven-metadata/v/https/repo1.maven.org/maven2/net/luckperms/api/maven-metadata.xml.svg?label=maven%20central&colorB=brightgreen)](https://search.maven.org/artifact/net.luckperms/api)
[![Discord](https://img.shields.io/discord/241667244927483904.svg?label=discord&logo=discord)](https://discord.gg/luckperms)

> **This is a fork of [LuckPerms](https://github.com/LuckPerms/LuckPerms) with added support for [Folia](https://github.com/PaperMC/Folia)**, the regionalized multithreading server software by PaperMC. See [flennium/folia-luckperms](https://github.com/flennium/folia-luckperms) for releases.

## What's different in this fork

Folia uses a regionalized threading model where each region of the world runs on its own thread, which breaks the standard Bukkit scheduler API. This fork adds a `FoliaSchedulerAdapter` that uses Folia's region-aware scheduler APIs so LuckPerms works correctly on Folia servers without any hacks or workarounds.

Changes include:
- New `folia/` module with a dedicated Folia build target
- `FoliaSchedulerAdapter` — replaces `BukkitSchedulerAdapter` on Folia, routing tasks through the correct regional/global/async schedulers
- Detection logic in `LPBukkitBootstrap` to automatically use the Folia scheduler when running on a Folia server
- Refactored `SchedulerAdapter` and `JavaSchedulerAdapter` shared across all platforms to support this cleanly

To use this on a Folia server, drop in the jar from this fork instead of the standard LuckPerms jar. Everything else works the same.

---

LuckPerms is a permissions plugin for Minecraft servers. It allows server admins to control what features players can use by creating groups and assigning permissions.

The latest downloads, wiki & other useful links can be found on the project homepage at [luckperms.net](https://luckperms.net/).

It is:

* **fast** - written with performance and scalability in mind.
* **reliable** - trusted by thousands of server admins, and the largest of server networks.
* **easy to use** - setup permissions using commands, directly in config files, or using the web editor.
* **flexible** - supports a variety of data storage options, and works on lots of different server types.
* **extensive** - a plethora of customization options and settings which can be changed to suit your server.
* **free** - available for download and usage at no cost, and permissively licensed so it can remain free forever.

For more information, see the wiki article on [Why LuckPerms?](https://luckperms.net/wiki/Why-LuckPerms)

## Building
LuckPerms uses Gradle to handle dependencies & building.

#### Requirements
* Java 21 JDK or newer
* Git

#### Compiling from source
```sh
git clone https://github.com/LuckPerms/LuckPerms.git
cd LuckPerms/
./gradlew build
```

You can find the output jars in the `loader/build/libs` or `build/libs` directories.

## Tests
There are some automated tests which run during each build.

* Unit tests are defined in [`common/src/test`](https://github.com/LuckPerms/LuckPerms/tree/master/common/src/test)
* Integration tests are defined in [`standalone/src/test`](https://github.com/LuckPerms/LuckPerms/tree/master/standalone/src/test).

## Contributing
#### Pull Requests
If you make any changes or improvements to the plugin which you think would be beneficial to others, please consider making a pull request to merge your changes back into the upstream project. (especially if your changes are bug fixes!)

LuckPerms loosely follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Generally, try to copy the style of code found in the class you're editing. 

#### Project Layout
The project is split up into a few separate modules.

* **API** - The public, semantically versioned API used by other plugins wishing to integrate with and retrieve data from LuckPerms. This module (for the most part) does not contain any implementation itself, and is provided by the plugin.
* **Common** - The common module contains most of the code which implements the respective LuckPerms plugins. This abstract module reduces duplicated code throughout the project.
* **Bukkit, BungeeCord, Fabric, Folia, Forge, Nukkit, Sponge & Velocity** - Each use the common module to implement plugins on the respective server platforms. The Folia module is specific to this fork and targets Folia's regionalized threading model.

## License
LuckPerms is licensed under the permissive MIT license. Please see [`LICENSE.txt`](https://github.com/LuckPerms/LuckPerms/blob/master/LICENSE.txt) for more info.
