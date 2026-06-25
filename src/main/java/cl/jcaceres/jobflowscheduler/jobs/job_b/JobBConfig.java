package cl.jcaceres.jobflowscheduler.jobs.job_b;

import lombok.Data;

/**
 * JobBConfig - Modelo de configuración específico de Job B
 * 
 * Encapsula todas las propiedades requeridas por Job B
 */
@Data
public class JobBConfig {
    
    // API Configuration
    private String apiEndpoint;
    private String apiKey;
    private Integer apiTimeout;
    private Integer apiRetries;
    
    // Job Parameters
    private boolean enabled;
    private String name;
    private Integer bufferSize;
    private Integer chunkSize;
    private String notifyEmail;
}
