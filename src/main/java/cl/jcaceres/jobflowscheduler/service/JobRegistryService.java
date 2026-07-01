package cl.jcaceres.jobflowscheduler.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.jcaceres.jobflowscheduler.model.JobConfig;
import cl.jcaceres.jobflowscheduler.model.JobConfigList;
import jakarta.annotation.PostConstruct;

@Service
public class JobRegistryService {

    private static final Logger log = LoggerFactory.getLogger(JobRegistryService.class);

    private final Scheduler scheduler;
    private final JobConfigManager jobConfigManager;

    public JobRegistryService(Scheduler scheduler, JobConfigManager jobConfigManager) {
        this.scheduler = scheduler;
        this.jobConfigManager = jobConfigManager;
    }

    @PostConstruct
    public void init() {
        registerConfiguredJobs();
        startScheduler();
    }

    public void registerConfiguredJobs() {

        try {
            JobConfigList configList = jobConfigManager.loadCOnfig();

            System.out.println("\n=== Registrando Jobs ===");
            int jobCount = 0;

            for (JobConfig config : configList.getJobs()) {

                if (!config.isEnabled()) {
                    System.out.println("Job deshabilitado: " + config.getName());
                    continue;
                }

                Class<? extends Job> jobClass = loadClass(config.getClassName());

                boolean loggingEnabled = config.getLogging() != null && config.getLogging().isEnabled();

                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put("loggingEnabled", loggingEnabled);

                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(config.getName())
                        .storeDurably(false)
                        .usingJobData(jobDataMap)
                        .build();

                // Creamos un trigger con el cron configurado
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(config.getName() + "_trigger")
                        .withSchedule(CronScheduleBuilder.cronSchedule(config.getCron())).build();

                // Registramos job en Quartz
                scheduler.scheduleJob(jobDetail, trigger);

                System.out.println("✓ Job registrado: " + config.getName() + " | Cron: " + config.getCron() + 
                                   " | Config Path: " + config.getConfigPath());
                jobCount++;
            }

            System.out.println("Total de jobs registrados: " + jobCount);
            System.out.println("=======================\n");

        } catch (Exception e) {
            throw new RuntimeException("Error registering jobs", e);
        }

    }

    private void startScheduler() {
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
                System.out.println("Quartz Scheduler iniciado correctamente.\n");
                log.info("Los logs detallados de cada job se encuentran en logs/<jobName>.log (si el logging está habilitado)");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar el Scheduler", e);
        }
    }

    public Class<? extends Job> loadClass(String className) throws ClassNotFoundException {
        return Class.forName(className).asSubclass(Job.class);
    }
}
