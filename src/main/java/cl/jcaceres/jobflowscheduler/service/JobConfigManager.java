package cl.jcaceres.jobflowscheduler.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.jcaceres.jobflowscheduler.model.JobConfigList;

/**
 * Gestor de configuración de jobs.
 * Carga la configuración JSON desde el classpath.
 */
@Service
public class JobConfigManager {

    private static final String CONFIG_FILE = "jobs-config.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Carga la configuración de jobs desde el archivo JSON en el classpath.
     */
    public JobConfigList loadCOnfig() throws IOException {

        ClassPathResource resource = new ClassPathResource(CONFIG_FILE);

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, JobConfigList.class);

        }

    }

}

