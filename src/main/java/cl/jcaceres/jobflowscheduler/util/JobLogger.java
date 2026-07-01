package cl.jcaceres.jobflowscheduler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class JobLogger {

    private final Logger logger;
    private final String jobName;
    private final boolean enabled;

    public JobLogger(String jobName, boolean enabled) {
        this.jobName = jobName;
        this.enabled = enabled;
        this.logger = LoggerFactory.getLogger(JobLogger.class);
    }

    public void info(String message) {
        if (!enabled) return;
        MDC.put("jobName", jobName);
        logger.info(message);
        MDC.remove("jobName");
    }

    public void info(String format, Object... args) {
        if (!enabled) return;
        MDC.put("jobName", jobName);
        logger.info(format, args);
        MDC.remove("jobName");
    }

    public void error(String message) {
        if (!enabled) return;
        MDC.put("jobName", jobName);
        logger.error(message);
        MDC.remove("jobName");
    }

    public void error(String message, Throwable t) {
        if (!enabled) return;
        MDC.put("jobName", jobName);
        logger.error(message, t);
        MDC.remove("jobName");
    }

    public void warn(String message) {
        if (!enabled) return;
        MDC.put("jobName", jobName);
        logger.warn(message);
        MDC.remove("jobName");
    }

    public void warn(String format, Object... args) {
        if (!enabled) return;
        MDC.put("jobName", jobName);
        logger.warn(format, args);
        MDC.remove("jobName");
    }

    public boolean isEnabled() {
        return enabled;
    }
}
