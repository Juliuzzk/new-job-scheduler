# Crear un Nuevo Job

## 1. Crear el Paquete

```bash
mkdir -p src/main/java/cl/jcaceres/jobscheduler/jobs/job_c
mkdir -p config/job-c
```

## 2. Crear Tres Clases

### JobCExecutor.java
```java
package cl.jcaceres.jobflowscheduler.jobs.job_c;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobCExecutor implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobCService service = new JobCService();
        try {
            service.execute();
        } catch (Exception e) {
            throw new JobExecutionException("Error en Job C: " + e.getMessage(), e);
        }
    }
}
```

### JobCService.java
```java
package cl.jcaceres.jobflowscheduler.jobs.job_c;

import java.io.IOException;
import java.util.Map;
import cl.jcaceres.jobflowscheduler.util.PropertyReader;

public class JobCService {
    private static final String CONFIG_FILE = "config/job-c/config.properties";

    public void execute() throws IOException {
        System.out.println("\n========== JOB C EJECUTÁNDOSE ==========");
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        
        Map<String, Object> config = PropertyReader.readConfig(CONFIG_FILE);
        
        System.out.println("Configuración cargada:");
        config.forEach((k, v) -> System.out.println("  - " + k + ": " + v));
        
        // Tu lógica aquí
        System.out.println("Job C completado.\n");
    }
}
```

### JobCConfig.java
```java
package cl.jcaceres.jobflowscheduler.jobs.job_c;

import lombok.Data;

@Data
public class JobCConfig {
    private String propiedad1;
    private String propiedad2;
}
```

## 3. Crear Archivo de Configuración

**config/job-c/config.properties:**
```properties
propiedad1=valor1
propiedad2=valor2
```

## 4. Registrar en jobs-config.json

```json
{
  "jobs": [
    {
      "name": "jobC",
      "className": "cl.jcaceres.jobflowscheduler.jobs.job_c.JobCExecutor",
      "cron": "0 0 * * * ?",
      "enabled": true,
      "configPath": "config/job-c"
    }
  ]
}
```

## 5. Compilar y Ejecutar

```bash
mvn clean compile
mvn spring-boot:run
```

## Rutas Multiplataforma

Las rutas se resuelven automáticamente para Windows y Linux.

**Configuración:**
```bash
# Linux - Variable de entorno
export JOB_SCHEDULER_CONFIG_DIR=/var/job-scheduler/config

# Windows - Variable de entorno
set JOB_SCHEDULER_CONFIG_DIR=C:\job-scheduler\config

# O ruta relativa (default)
config/job-c/config.properties
```

## Expresiones Cron

| Expresión | Significado |
|-----------|------------|
| `0/15 * * * * ?` | Cada 15 segundos |
| `0 0 * * * ?` | Cada día a las 00:00 |
| `0 0 0 ? * MON` | Cada lunes |
| `0 0 12 * * ?` | Cada día al mediodía |
