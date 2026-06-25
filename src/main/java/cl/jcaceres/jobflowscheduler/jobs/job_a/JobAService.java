package cl.jcaceres.jobflowscheduler.jobs.job_a;

import java.io.IOException;
import java.util.Map;

import cl.jcaceres.jobflowscheduler.util.PropertyReader;

/**
 * JobAService - Contiene la lógica específica de Job A
 * 
 * Responsabilidades:
 * - Cargar y validar configuración desde application.properties
 * - Ejecutar la lógica de procesamiento de datos
 * - Logging detallado de ejecución
 */
public class JobAService {

    private static final String CONFIG_FILE = "config/job-a/application.properties";

    /**
     * Ejecuta el procesamiento de Job A
     */
    public void execute() throws IOException, InterruptedException {
        
        System.out.println("\n========== JOB A EJECUTÁNDOSE ==========");
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        
        // Cargar configuración
        JobAConfig config = loadConfig();
        
        // Mostrar configuración cargada
        printConfig(config);
        
        // Simular procesamiento
        System.out.println("\nProcesando datos con configuración de Job A...");
        Thread.sleep(500);
        
        System.out.println("Job A completado exitosamente.");
        System.out.println("========================================\n");
    }

    /**
     * Carga la configuración desde el archivo de propiedades
     */
    private JobAConfig loadConfig() throws IOException {
        
        Map<String, Object> configMap = PropertyReader.readConfig(CONFIG_FILE);
        
        JobAConfig config = new JobAConfig();
        config.setDatabaseHost((String) configMap.get("database.host"));
        config.setDatabasePort((String) configMap.get("database.port"));
        config.setDatabaseName((String) configMap.get("database.name"));
        config.setDatabaseUser((String) configMap.get("database.user"));
        config.setDatabasePassword((String) configMap.get("database.password"));
        config.setRetries(Integer.parseInt((String) configMap.get("job.a.retries")));
        config.setTimeout(Integer.parseInt((String) configMap.get("job.a.timeout")));
        config.setBatchSize(Integer.parseInt((String) configMap.get("job.a.batch.size")));
        
        return config;
    }

    /**
     * Imprime la configuración cargada
     */
    private void printConfig(JobAConfig config) {
        
        System.out.println("\nConfiguraciones cargadas desde: " + CONFIG_FILE);
        System.out.println("  - Database Host: " + config.getDatabaseHost());
        System.out.println("  - Database Port: " + config.getDatabasePort());
        System.out.println("  - Database Name: " + config.getDatabaseName());
        System.out.println("  - Database User: " + config.getDatabaseUser());
        System.out.println("  - Job A Retries: " + config.getRetries());
        System.out.println("  - Job A Timeout: " + config.getTimeout() + "ms");
        System.out.println("  - Job A Batch Size: " + config.getBatchSize());
    }
}
