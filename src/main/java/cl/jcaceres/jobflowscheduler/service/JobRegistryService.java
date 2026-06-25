package cl.jcaceres.jobflowscheduler.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import cl.jcaceres.jobflowscheduler.model.JobConfig;
import cl.jcaceres.jobflowscheduler.model.JobConfigList;
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
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar el Scheduler", e);
        }
    }

    public Class<? extends Job> loadClass(String className) throws ClassNotFoundException {
        return Class.forName(className).asSubclass(Job.class);
    }
}
