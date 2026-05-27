package cl.jcaceres.jobscheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExampleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("ExampleJob ejecutándose en: " + java.time.LocalDateTime.now());
    }

}
