# About
CommandsPlus (Commands+) is an open-source plugin made by 2 Israeli developers, to help make better quality plugins, with readable functionality, small usable API, and a continuous updating schedule to make sure it'll fit in a good spot specifically for every Spigot/Paper server.

# Distribution
This plugin is completely open-source, and we're happy for anyone to take on a fork/join our team to make better competitive marketing for free software plugins!
as of now, we're not looking to make the plugin a premium software plugin or anything to do with purchasing the rights to own a copy, therefor we're licensing via MIT protocol for professional causes.

# Compiling
Compilation requires JDK 12 and up.
2 Ways to compiling the plugin:
- run `./gradlew build` from the terminal executed from your plugin's folder.
- Use your IDE's standard Gradle Integration Tool to build the jar via `Tasks -> Build -> Jar` (If you're using **Eclipse** I highly encourage you to make sure all dependencies are queried inside the system, due to Eclipse not being accurate/familiar with external build tools). 
Once the plugin compiles, grab the jar from `~/output` folder.

### Gradle Features
Haven't noticed yet? we use Gradle (Groovy based) and not Maven, means you can redepend some dependencies into Commands+, add more stuff then there actually is, and have a lot of integrations inside this plugin, it is quite customized in the Punishment System side, and we're currently working on customizing the Command system to make it work super duper easy.

### Code default requirements
By default our code is designed with the `SpigotAPI` (not Paper sadly), and `Lombok`.
If you are not familiar with Lombok and its benefits ***WE HIGHLY ENCOURAGE YOU*** to read up about the [Lombok project](https://projectlombok.org).
### IntelliJ Users
If you're using IntelliJ, please make sure you're having [Minecraft Integrations Plugin](https://plugins.jetbrains.com/plugin/8327-minecraft-development) for IntelliJ installed, to make the best out of your Gradle Compiling experience for this plugin.

# Issues
If you're going through an issue/error that is unexplainable please open a new issue at the [Issues Tab](https://github.com/ofirtim/CommandsPlus/issues).

Please make sure when you post the issue to include the following information:
- Server Software Version + Build Number.
- Plugin Version (Notice some bugs can accure due to not updating the plugin into newer versions).
- Error from console/proof of bugs.
- Simple logics of information according to what happened that caused the bug/issue.

# Support & Contribution
If you have downloaded the plugin, and felt like this plugin helped you through, and you're willing to help/support/Contribute, you can help the creators of this plugin by the following ways:
- Create a pull request, if you find any interest in the plugin to make new features or found a bug that you found a fix for already.
- Suggest new ideas and make polls, with creative members of the MC Community.
