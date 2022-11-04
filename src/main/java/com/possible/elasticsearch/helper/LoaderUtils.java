package com.possible.elasticsearch.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class LoaderUtils {
    private static final Logger LOG = LoggerFactory.getLogger(LoaderUtils.class);

    private LoaderUtils() {
    }

    public static String loadAsString(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();
            return new String(Files.readAllBytes(resource.toPath()));
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            return null;
        }

    }

    public static InputStream getFileFromResourceAsStream(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();
            return Files.newInputStream(resource.toPath());
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            return null;
        }

    }
}
