package cl.jcaceres.jobflowscheduler.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.jcaceres.jobflowscheduler.model.JobConfigList;

@Service
public class JobConfigManager {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jobs.config.path:classpath:jobs-config.json}")
    private String configPath;

    public JobConfigList loadCOnfig() throws IOException {
        Resource resource;

        if (configPath.startsWith("classpath:")) {
            resource = new ClassPathResource(configPath.substring("classpath:".length()));
        } else if (configPath.startsWith("file:")) {
            resource = new FileSystemResource(configPath.substring("file:".length()));
        } else {
            resource = new FileSystemResource(configPath);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, JobConfigList.class);
        }
    }

}

