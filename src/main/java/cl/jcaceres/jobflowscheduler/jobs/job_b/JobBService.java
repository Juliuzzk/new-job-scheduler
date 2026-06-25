package cl.jcaceres.jobflowscheduler.jobs.job_b;

import java.io.IOException;
import java.util.Map;

import cl.jcaceres.jobflowscheduler.util.PropertyReader;

/**
 * JobBService - Contiene la lógica específica de Job B
 * 
 * Responsabilidades:
 * - Cargar y validar configuración desde config.yaml
 * - Ejecutar la lógica de integración con API
 * - Logging detallado de ejecución
 */
public class JobBService {

    private static final String CONFIG_FILE = "config/job-b/config.yaml";

    /**
     * Ejecuta el procesamiento de Job B
     */
    public void execute() throws IOException, InterruptedException {
        
        System.out.println("\n========== JOB B EJECUTÁNDOSE ==========");
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        
        // Cargar configuración
        JobBConfig config = loadConfig();
        
        // Mostrar configuración cargada
        printConfig(config);
        
        // Simular procesamiento
        System.out.println("\nIntegrando con API usando configuración de Job B...");
        Thread.sleep(500);
        
        System.out.println("Job B completado exitosamente. Notificación enviada a: " + config.getNotifyEmail());
        System.out.println("========================================\n");
    }

    /**
     * Carga la configuración desde el archivo YAML
     */
    @SuppressWarnings("unchecked")
    private JobBConfig loadConfig() throws IOException {
        
        Map<String, Object> configMap = PropertyReader.readConfig(CONFIG_FILE);
        
        // Extraer configuración de API
        Map<String, Object> apiConfig = (Map<String, Object>) configMap.get("api");
        String apiEndpoint = (String) apiConfig.get("endpoint");
        String apiKey = (String) apiConfig.get("key");
        Integer apiTimeout = (Integer) apiConfig.get("timeout");
        Integer apiRetries = (Integer) apiConfig.get("retries");
        
        // Extraer configuración de Job B
        Map<String, Object> jobBConfig = (Map<String, Object>) configMap.get("job-b");
        Boolean jobBEnabled = (Boolean) jobBConfig.get("enabled");
        String jobBName = (String) jobBConfig.get("name");
        Integer bufferSize = (Integer) jobBConfig.get("buffer-size");
        Integer chunkSize = (Integer) jobBConfig.get("chunk-size");
        String notifyEmail = (String) jobBConfig.get("notify-email");
        
        // Construir objeto de configuración
        JobBConfig config = new JobBConfig();
        config.setApiEndpoint(apiEndpoint);
        config.setApiKey(apiKey);
        config.setApiTimeout(apiTimeout);
        config.setApiRetries(apiRetries);
        config.setEnabled(jobBEnabled);
        config.setName(jobBName);
        config.setBufferSize(bufferSize);
        config.setChunkSize(chunkSize);
        config.setNotifyEmail(notifyEmail);
        
        return config;
    }

    /**
     * Imprime la configuración cargada
     */
    private void printConfig(JobBConfig config) {
        
        System.out.println("\nConfiguraciones cargadas desde: " + CONFIG_FILE);
        System.out.println("\nAPI Configuration:");
        System.out.println("  - Endpoint: " + config.getApiEndpoint());
        System.out.println("  - API Key: " + config.getApiKey());
        System.out.println("  - Timeout: " + config.getApiTimeout() + "ms");
        System.out.println("  - Retries: " + config.getApiRetries());
        
        System.out.println("\nJob B Configuration:");
        System.out.println("  - Enabled: " + config.isEnabled());
        System.out.println("  - Name: " + config.getName());
        System.out.println("  - Buffer Size: " + config.getBufferSize());
        System.out.println("  - Chunk Size: " + config.getChunkSize());
        System.out.println("  - Notify Email: " + config.getNotifyEmail());
    }
}
