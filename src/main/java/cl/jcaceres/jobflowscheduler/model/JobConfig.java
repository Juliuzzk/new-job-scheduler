package cl.jcaceres.jobflowscheduler.model;

import lombok.Data;

@Data
public class JobConfig {

    private String name;
    private String className;
    private String cron;
    private boolean enabled;
    private String configPath;
    private LoggingConfig logging;

    @Data
    public static class LoggingConfig {
        private boolean enabled;
    }
}
