package cl.jcaceres.jobscheduler.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
// @Configuration indica que esta clase contiene métodos @Bean.
// Es decir, aquí declaramos objetos que Spring debe crear y administrar.
public class QuartzConfig {

    @Bean
    // @Bean le dice a Spring:
    // “El objeto que retorna este método debe vivir dentro del contenedor de
    // Spring”.
    // Spring ejecuta este método una sola vez y guarda el resultado como un bean
    // singleton.
    public SchedulerFactoryBean schedulerFactoryBean() {
        // SchedulerFactoryBean es la fábrica oficial de Quartz dentro de Spring.
        // Se encarga de crear y configurar el Scheduler real.
        return new SchedulerFactoryBean();
    }

    @Bean
    // Otro @Bean: ahora creamos el Scheduler real.
    // Spring inyecta automáticamente el SchedulerFactoryBean como parámetro.
    public Scheduler scheduler(SchedulerFactoryBean factory) throws SchedulerException {

        // Obtenemos el Scheduler real desde la fábrica.
        // Este es el motor que ejecutará los jobs y manejará los triggers.
        Scheduler scheduler = factory.getScheduler();

        // Arranca Quartz: crea hilos internos y comienza a ejecutar los jobs agendados.
        scheduler.start();

        // Retornamos el Scheduler para que Spring lo registre como bean
        // y pueda inyectarlo en JobRegistryService, por ejemplo.
        return scheduler;
    }
}
