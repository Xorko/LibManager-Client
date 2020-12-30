# LibManager
### A software to manage a library
##### Université de Bretagne-Sud - INF1503 - 2020

### How to run ?
* Windows: `gradlew.bat run`
* Linux/macOS: `./gradlew run`

### First-time use
* Fill `src/main/resources/defaultconfig.properties` with your configuration.

Your configuration will be loaded in the app preferences.

### Can't connect to the server ?
* Fill `src/main/resources/defaultconfig.properties` with your data
* Run the application with argument `--args='--reset-configuration'` (e.g. `./gradlew run --args='--reset-configuration'`).

### Add custom components to Scene Builder
Import `lib/LibManager-components.jar` into Scene Builder (Scene Builder 11 or higher required)

### Authors
* [Thibault LE GOFF](https://www.github.com/Xorko)
* [Benoit AGOGUE](https://github.com/BzhNouille)