package cl.jcaceres.jobflowscheduler.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilidad para resolver rutas de configuración de forma multiplataforma.
 * Soporta rutas relativas y absolutas en Windows y Linux.
 */
public class PathResolver {

    /**
     * Resuelve una ruta de configuración de forma multiplataforma.
     * 
     * Acepta:
     * - Rutas relativas: "config/job-a" (desde el directorio de ejecución)
     * - Rutas absolutas Windows: "C:\\config\\job-a"
     * - Rutas absolutas Linux: "/var/job-scheduler/config/job-a"
     * - Variables de entorno: "${CONFIG_DIR}/job-a"
     * 
     * @param configPath Ruta de configuración
     * @return Ruta resuelta como String
     */
    public static String resolve(String configPath) {
        if (configPath == null || configPath.isEmpty()) {
            throw new IllegalArgumentException("configPath no puede ser nulo o vacío");
        }

        // Reemplazar variables de entorno
        String resolvedPath = resolveEnvironmentVariables(configPath);

        // Convertir a Path (maneja separadores automáticamente)
        Path path = Paths.get(resolvedPath);

        // Retornar como String normalizado
        return path.toAbsolutePath().toString();
    }

    /**
     * Resuelve variables de entorno en formato ${VAR_NAME}.
     */
    private static String resolveEnvironmentVariables(String path) {
        String result = path;
        
        // Buscar patrones ${VAR_NAME}
        int start = result.indexOf("${");
        while (start != -1) {
            int end = result.indexOf("}", start);
            if (end != -1) {
                String varName = result.substring(start + 2, end);
                String varValue = System.getenv(varName);
                
                if (varValue != null) {
                    result = result.substring(0, start) + varValue + result.substring(end + 1);
                } else {
                    throw new IllegalArgumentException("Variable de entorno no encontrada: " + varName);
                }
                
                start = result.indexOf("${");
            } else {
                break;
            }
        }
        
        return result;
    }

    /**
     * Obtiene la ruta base de configuración desde variable de entorno o valor por defecto.
     * 
     * Busca en orden:
     * 1. Variable de entorno: JOB_SCHEDULER_CONFIG_DIR
     * 2. Propiedad de sistema: job.scheduler.config.dir
     * 3. Ruta por defecto: "config"
     * 
     * @return Ruta base de configuración resuelta
     */
    public static String getConfigBaseDir() {
        // 1. Variable de entorno
        String envPath = System.getenv("JOB_SCHEDULER_CONFIG_DIR");
        if (envPath != null && !envPath.isEmpty()) {
            return resolve(envPath);
        }

        // 2. Propiedad de sistema
        String sysProp = System.getProperty("job.scheduler.config.dir");
        if (sysProp != null && !sysProp.isEmpty()) {
            return resolve(sysProp);
        }

        // 3. Ruta por defecto relativa
        return resolve("config");
    }

    /**
     * Construye la ruta completa a un archivo de configuración de un job.
     * 
     * @param jobName Nombre del job (ej: "job-a")
     * @param fileName Nombre del archivo (ej: "application.properties")
     * @return Ruta completa resuelta
     */
    public static String getJobConfigFile(String jobName, String fileName) {
        String baseDir = getConfigBaseDir();
        Path jobConfigPath = Paths.get(baseDir, jobName, fileName);
        return jobConfigPath.toAbsolutePath().toString();
    }
}
