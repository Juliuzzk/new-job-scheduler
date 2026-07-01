package cl.jcaceres.jobflowscheduler.jobs.job_a;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cl.jcaceres.jobflowscheduler.util.JobLogger;

public class JobAExecutor implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        boolean loggingEnabled = context.getMergedJobDataMap().getBoolean("loggingEnabled");
        JobLogger log = new JobLogger("jobA", loggingEnabled);

        JobAService service = new JobAService(log);

        try {
            service.execute();
        } catch (Exception e) {
            log.error("Error en Job A: " + e.getMessage());
            throw new JobExecutionException("Error en Job A: " + e.getMessage(), e);
        }
    }
}
