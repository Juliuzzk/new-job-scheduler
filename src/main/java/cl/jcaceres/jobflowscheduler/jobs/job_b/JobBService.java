package cl.jcaceres.jobflowscheduler.jobs.job_b;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.jcaceres.jobflowscheduler.service.DatabaseService;
import cl.jcaceres.jobflowscheduler.util.JobLogger;
import cl.jcaceres.jobflowscheduler.util.PropertyReader;
import cl.jcaceres.jobflowscheduler.util.SqlReader;

public class JobBService {

    private static final Logger heartbeat = LoggerFactory.getLogger("heartbeat");
    private static final String CONFIG_FILE = "config/job-b/config.yaml";
    private static final String SQL_FILE = "cl/jcaceres/jobflowscheduler/jobs/job_b/sql/queries.sql";
    private static final String SQL_QUERIES = SqlReader.readSQLResource(SQL_FILE);

    private final JobLogger log;

    public JobBService(JobLogger log) {
        this.log = log;
    }

    public void execute() throws IOException, InterruptedException {

        log.info("========== JOB B EJECUTÁNDOSE ==========");
        log.info("Timestamp: {}", java.time.LocalDateTime.now());

        JobBConfig config = loadConfig();

        printConfig(config);

        log.info("Ejecutando queries en base de datos...");
        DatabaseService.executeQueries(
                config.getDatabaseHost(),
                config.getDatabasePort(),
                config.getDatabaseName(),
                config.getDatabaseUser(),
                config.getDatabasePassword(),
                SQL_QUERIES,
                log
        );

        log.info("Job B completado exitosamente. Notificación enviada a: {}", config.getNotifyEmail());
        log.info("========================================");

        heartbeat.info("jobB ejecutado correctamente");
    }

    @SuppressWarnings("unchecked")
    private JobBConfig loadConfig() throws IOException {

        Map<String, Object> configMap = PropertyReader.readConfig(CONFIG_FILE);

        Map<String, Object> dbConfig = (Map<String, Object>) configMap.get("database");
        String dbHost = (String) dbConfig.get("host");
        int dbPort = (Integer) dbConfig.get("port");
        String dbName = (String) dbConfig.get("name");
        String dbUser = (String) dbConfig.get("user");
        String dbPassword = (String) dbConfig.get("password");

        Map<String, Object> apiConfig = (Map<String, Object>) configMap.get("api");
        String apiEndpoint = (String) apiConfig.get("endpoint");
        String apiKey = (String) apiConfig.get("key");
        Integer apiTimeout = (Integer) apiConfig.get("timeout");
        Integer apiRetries = (Integer) apiConfig.get("retries");

        Map<String, Object> jobBConfig = (Map<String, Object>) configMap.get("job-b");
        Boolean jobBEnabled = (Boolean) jobBConfig.get("enabled");
        String jobBName = (String) jobBConfig.get("name");
        Integer bufferSize = (Integer) jobBConfig.get("buffer-size");
        Integer chunkSize = (Integer) jobBConfig.get("chunk-size");
        String notifyEmail = (String) jobBConfig.get("notify-email");

        JobBConfig config = new JobBConfig();
        config.setDatabaseHost(dbHost);
        config.setDatabasePort(dbPort);
        config.setDatabaseName(dbName);
        config.setDatabaseUser(dbUser);
        config.setDatabasePassword(dbPassword);
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

    private void printConfig(JobBConfig config) {

        log.info("Configuraciones cargadas desde: {}", CONFIG_FILE);
        log.info("Database Configuration:");
        log.info("  - Host: {}", config.getDatabaseHost());
        log.info("  - Port: {}", config.getDatabasePort());
        log.info("  - Name: {}", config.getDatabaseName());
        log.info("  - User: {}", config.getDatabaseUser());
        log.info("API Configuration:");
        log.info("  - Endpoint: {}", config.getApiEndpoint());
        log.info("  - Timeout: {}ms", config.getApiTimeout());
        log.info("  - Retries: {}", config.getApiRetries());
        log.info("Job B Configuration:");
        log.info("  - Name: {}", config.getName());
        log.info("  - Buffer Size: {}", config.getBufferSize());
        log.info("  - Chunk Size: {}", config.getChunkSize());
        log.info("  - Notify Email: {}", config.getNotifyEmail());
    }
}
