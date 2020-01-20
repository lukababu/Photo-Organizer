# Photo Organizer

This little script helps to rename, sort, and move your photos into an organized folder structure.

At first I used [DropIT](http://www.dropitproject.com/), but the major problem with that is the nonexistent usa of EXIF metadata within the photos.


The motivation and the output folder structure was optimized for Plex Media Server. Unfortunately, Plex no longer has a guide posted on their [webpage](https://support.plex.tv/articles/naming-and-organizing-your-movie-media-files) on how to structure a photo library folder.

Folder Organization structure:
```$xslt
root
|--2017
    |-- August
        |-- 07 2017-08-07 18.32.58.jpg
        |-- 21 2017-08-21 10.14.36.jpg
    |-- June
        |-- 01
            |-- 2017-06-01 16.07.45.jpg
```

## Getting Started

I will not take responsibility of lost or mismanaged photos from the use of this software.

That being said it is performing as expected in my small scale tests on Windows.

### Prerequisites

* You need have Java JDK installed
* Windows System Environment configured to the JDK (optional)
* IDEA InteliJ


### Installing

1. Open the project in IntelliJ

2. After cloning the repository you will want to configure the config.yml file according to your needs 

    Example:
    ```
      - source: "C:\\Users\\l_ire\\Desktop\\photos"
        destination: "C:\\Users\\l_ire\\Desktop\\photos\\Named"
        flag: "s"
        extension:
          - "jpg"
          - "jpeg"
    ```

3. Edit configuration from the top right dropdown as:

    ```
    Main class:         com.company.Main
    Program Argument:   C:\Users\l_ire\Documents\ContentOrganizer\src\main\java\config.yml
    ```
4. Run 'Main' useing the Play button or by hitting Shift + F10

## Testing

Hope to add testing later on when.

## Built With

* [metadata-extractor](https://github.com/drewnoakes/metadata-extractor) - Extracting the metadata from photos
* [SnakeYAML](https://mvnrepository.com/artifact/org.yaml/snakeyaml) - To parse yaml file
* [Apache Commons](https://commons.apache.org/) - Used for file manipulation
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Luke Iremadze** - *Initial work* - [Lukababu](https://github.com/Lukababu)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

