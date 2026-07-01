package cl.jcaceres.jobflowscheduler.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;

public class SqlReader {

    private SqlReader() {
    }

    public static String readSQLResource(String classpathPath) {
        try {
            ClassPathResource resource = new ClassPathResource(classpathPath);
            try (InputStream is = resource.getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer recurso SQL: " + classpathPath, e);
        }
    }
}
