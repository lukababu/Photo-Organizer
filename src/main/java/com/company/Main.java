package com.company;
import com.company.configuration.Config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        Photos photos;
        String[] extensions;
        Yaml yaml;
        Config config = null;
        if( args.length != 1 ) {
            System.out.println( "Usage: <config.yml>" );
            logger.debug("Invalid argument length of " + args.length);
            return;
        }

        Constructor constructor = new Constructor(Config.class); // Config.class is root
        yaml = new Yaml(constructor);

        try( InputStream in = Files.newInputStream( Paths.get( args[0] ) ) ) {
            config = yaml.load(in);
            logger.debug("Configuration loaded: ");
            logger.debug(config.toString());
            logger.debug(config.getTasks().get(0).getDestination());

            for (int i = 0; i < config.getTasks().size(); i++) {
                extensions = Arrays.stream(config.getTasks().get(i).getExtension().toArray())
                        .map(String::valueOf).toArray(String[]::new);
                photos = new Photos(config.getTasks().get(i).getSource(),
                        extensions,
                        config.getTasks().get(i).getDestination(),
                        config.getTasks().get(i).getFlag());
                photos.process();
            }
        } catch (Exception e) {
            logger.debug("Could not load YAML configuration error: ");
            logger.debug("{}", e);
        }
    }
}
