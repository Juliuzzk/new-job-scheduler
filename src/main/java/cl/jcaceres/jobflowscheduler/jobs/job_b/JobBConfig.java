package cl.jcaceres.jobflowscheduler.jobs.job_b;

import lombok.Data;

@Data
public class JobBConfig {

    // Database
    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;

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
