# Ship browser
![Enterprise](https://github.com/MartinJoukl/ShipBrowser/assets/100210776/33f4a2d9-c804-40c8-9d55-502285e56be8)

Application is REST api. It's purpose is to provide list of Azur lane ships and their skins and skills.

## Requirements to run
Application root should be placed in additional folder - when obtaining ship images, new folder ../images is created in parent.
Docker is needed to run PostgreSQL DB in which data is stored - this container can be launched by calling docker/docker-compose.yaml.
When db starts application can be run.
## Initialize ship DB, Api
All api commands are stored in insomnia file - provided in src/test/resources/Insomnia_2024-05-06.json

Work begins by calling command "synchronizeShips" (you have to ligin first by calling login and put retrieved token to bearer).

This command synchronizes list of ships, skills and skins with azurApi. It also downloads images from the remove (note: you can download all images from https://github.com/AzurAPI/azurapi-js-setup/tree/master/images as zip and manually put it into ../images to save brandwidth).
## DB schema
![db_schema](https://github.com/MartinJoukl/ShipBrowser/assets/100210776/7851fbe8-7486-4da6-b7bf-cd5c230460f3)
