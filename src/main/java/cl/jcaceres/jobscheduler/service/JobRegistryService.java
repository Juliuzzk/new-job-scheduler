package cl.jcaceres.jobscheduler.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import cl.jcaceres.jobscheduler.model.JobConfig;
import cl.jcaceres.jobscheduler.model.JobConfigList;
import jakarta.annotation.PostConstruct;

@Service
public class JobRegistryService {

    private final Scheduler scheduler;
    private final JobConfigManager jobConfigManager;

    public JobRegistryService(Scheduler scheduler, JobConfigManager jobConfigManager) {
        this.scheduler = scheduler;
        this.jobConfigManager = jobConfigManager;
    }

    @PostConstruct
    public void init() {
        registerConfiguredJobs();
    }

    public void registerConfiguredJobs() {

        try {
            JobConfigList configList = jobConfigManager.loadCOnfig();

            for (JobConfig config : configList.getJobs()) {

                if (!config.isEnabled()) {
                    continue;
                }

                // Cargamos la clase del job
                Class<? extends Job> jobClass = loadClass(config.getClassName());

                // Creamos el job con su detalle
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(config.getName()).storeDurably(false)
                        .build();

                // Creamos un trigger con el cron configurado
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(config.getName() + "_trigger")
                        .withSchedule(CronScheduleBuilder.cronSchedule(config.getCron())).build();

                // Registramos job en Quartz
                scheduler.scheduleJob(jobDetail, trigger);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error registering jobs", e);
        }

    }

    public Class<? extends Job> loadClass(String className) throws ClassNotFoundException {
        return Class.forName(className).asSubclass(Job.class);
    }
}
