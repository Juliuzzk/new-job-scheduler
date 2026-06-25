package cl.jcaceres.jobflowscheduler.jobs.job_b;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job B - Ejemplo de job que lee configuración desde un archivo .yaml
 * 
 * Responsabilidades:
 * - Orquestar la lógica de integración con API
 * - Integrar con el servicio de configuración
 * - Manejar errores
 */
public class JobBExecutor implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        JobBService service = new JobBService();
        
        try {
            service.execute();
        } catch (Exception e) {
            System.err.println("Error en Job B: " + e.getMessage());
            throw new JobExecutionException("Error en Job B: " + e.getMessage(), e);
        }
    }
}
