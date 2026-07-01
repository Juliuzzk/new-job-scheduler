package cl.jcaceres.jobflowscheduler.jobs.job_b;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cl.jcaceres.jobflowscheduler.util.JobLogger;

public class JobBExecutor implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        boolean loggingEnabled = context.getMergedJobDataMap().getBoolean("loggingEnabled");
        JobLogger log = new JobLogger("jobB", loggingEnabled);

        JobBService service = new JobBService(log);

        try {
            service.execute();
        } catch (Exception e) {
            log.error("Error en Job B: " + e.getMessage());
            throw new JobExecutionException("Error en Job B: " + e.getMessage(), e);
        }
    }
}
