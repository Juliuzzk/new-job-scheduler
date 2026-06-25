package cl.jcaceres.jobflowscheduler.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

/**
 * Utility para leer archivos de configuración (.properties, .yaml, .yml)
 * desde el filesystem externo. Soporta rutas multiplataforma.
 */
public class PropertyReader {

    /**
     * Lee un archivo .properties y retorna un Map con sus valores.
     * La ruta es resuelta automáticamente para Windows y Linux.
     */
    public static Map<String, Object> readProperties(String filePath) throws IOException {
        String resolvedPath = PathResolver.resolve(filePath);
        Map<String, Object> result = new HashMap<>();
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(resolvedPath)) {
            props.load(fis);
            props.forEach((key, value) -> result.put((String) key, value));
        }

        return result;
    }

    /**
     * Lee un archivo .yaml o .yml y retorna un Map con sus valores.
     * La ruta es resuelta automáticamente para Windows y Linux.
     */
    public static Map<String, Object> readYaml(String filePath) throws IOException {
        String resolvedPath = PathResolver.resolve(filePath);
        Yaml yaml = new Yaml();
        String content = new String(Files.readAllBytes(Paths.get(resolvedPath)));
        Map<String, Object> result = yaml.load(content);
        return result != null ? result : new HashMap<>();
    }

    /**
     * Lee un archivo de configuración basado en su extensión.
     * Soporta: .properties, .yaml, .yml
     * La ruta es resuelta automáticamente para Windows y Linux.
     */
    public static Map<String, Object> readConfig(String filePath) throws IOException {
        String resolvedPath = PathResolver.resolve(filePath);

        if (!Files.exists(Paths.get(resolvedPath))) {
            throw new IOException("Archivo de configuración no encontrado: " + resolvedPath);
        }

        String fileName = Paths.get(resolvedPath).getFileName().toString().toLowerCase();

        if (fileName.endsWith(".properties")) {
            return readProperties(filePath);
        } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            return readYaml(filePath);
        } else {
            throw new IOException("Formato de archivo no soportado: " + resolvedPath + 
                    ". Use .properties, .yaml o .yml");
        }
    }

    /**
     * Lee un archivo .properties como Properties object (útil si necesitas usar directamente).
     * La ruta es resuelta automáticamente para Windows y Linux.
     */
    public static Properties readPropertiesAsObject(String filePath) throws IOException {
        String resolvedPath = PathResolver.resolve(filePath);
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(resolvedPath)) {
            props.load(fis);
        }

        return props;
    }
}
