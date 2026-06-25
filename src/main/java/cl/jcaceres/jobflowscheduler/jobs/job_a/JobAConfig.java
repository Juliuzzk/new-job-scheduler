package cl.jcaceres.jobflowscheduler.jobs.job_a;

import lombok.Data;

/**
 * JobAConfig - Modelo de configuración específico de Job A
 * 
 * Encapsula todas las propiedades requeridas por Job A
 */
@Data
public class JobAConfig {
    
    // Database Configuration
    private String databaseHost;
    private String databasePort;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;
    
    // Job Parameters
    private int retries;
    private int timeout;
    private int batchSize;
}
