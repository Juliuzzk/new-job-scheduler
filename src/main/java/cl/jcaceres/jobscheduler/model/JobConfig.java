package cl.jcaceres.jobscheduler.model;

import lombok.Data;

@Data
public class JobConfig {

    private String name;
    private String className;
    private String cron;
    private boolean enabled;
}
