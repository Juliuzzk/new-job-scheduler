package cl.jcaceres.jobflowscheduler.jobs.job_a;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.jcaceres.jobflowscheduler.service.DatabaseService;
import cl.jcaceres.jobflowscheduler.util.JobLogger;
import cl.jcaceres.jobflowscheduler.util.PropertyReader;
import cl.jcaceres.jobflowscheduler.util.SqlReader;

public class JobAService {

    private static final Logger heartbeat = LoggerFactory.getLogger("heartbeat");
    private static final String CONFIG_FILE = "config/job-a/application.properties";
    private static final String SQL_QUERIES = SqlReader
            .readSQLResource("cl/jcaceres/jobflowscheduler/jobs/job_a/sql/queries.sql");

    private final JobLogger log;

    public JobAService(JobLogger log) {
        this.log = log;
    }

    public void execute() throws IOException, InterruptedException {

        log.info("========== JOB A EJECUTÁNDOSE ==========");
        log.info("Timestamp: {}", java.time.LocalDateTime.now());

        JobAConfig config = loadConfig();

        printConfig(config);

        log.info("Ejecutando queries en base de datos...");
        DatabaseService.executeQueries(
                config.getDatabaseHost(),
                Integer.parseInt(config.getDatabasePort()),
                config.getDatabaseName(),
                config.getDatabaseUser(),
                config.getDatabasePassword(),
                SQL_QUERIES,
                log);

        log.info("Job A completado exitosamente.");
        log.info("========================================");

        heartbeat.info("jobA ejecutado correctamente");
    }

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

    private void printConfig(JobAConfig config) {

        log.info("Configuraciones cargadas desde: {}", CONFIG_FILE);
        log.info("  - Database Host: {}", config.getDatabaseHost());
        log.info("  - Database Port: {}", config.getDatabasePort());
        log.info("  - Database Name: {}", config.getDatabaseName());
        log.info("  - Database User: {}", config.getDatabaseUser());
        log.info("  - Job A Retries: {}", config.getRetries());
        log.info("  - Job A Timeout: {}ms", config.getTimeout());
        log.info("  - Job A Batch Size: {}", config.getBatchSize());
    }
}
