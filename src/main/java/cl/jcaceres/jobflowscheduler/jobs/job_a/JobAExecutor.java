package cl.jcaceres.jobflowscheduler.jobs.job_a;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job A - Ejemplo de job que lee configuración desde un archivo .properties
 * 
 * Responsabilidades:
 * - Orquestar la lógica de procesamiento de datos
 * - Integrar con el servicio de configuración
 * - Manejar errores
 */
public class JobAExecutor implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        JobAService service = new JobAService();
        
        try {
            service.execute();
        } catch (Exception e) {
            System.err.println("Error en Job A: " + e.getMessage());
            throw new JobExecutionException("Error en Job A: " + e.getMessage(), e);
        }
    }
}
