package org.yangyi.project.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    //  Properties对象
    private final Properties properties = new Properties();
    private String[] profiles = {
            "sharding-sphere.properties"
    };

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        for (String profile : profiles) {
            //  从classpath路径下面查找文件
            Resource resource = new ClassPathResource(profile);
            //  加载成PropertySource对象，并添加到Environment环境中
            environment.getPropertySources().addLast(loadProfiles(resource));
        }
    }

    //  加载单个配置文件
    private PropertySource<?> loadProfiles(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("配置文件 [" + resource.getFilename() + "] 不存在");
        }
        try {
            properties.load(resource.getInputStream());
            return new PropertiesPropertySource(Objects.requireNonNull(resource.getFilename()), properties);
        } catch (IOException ex) {
            throw new IllegalStateException("加载配置文件失败" + resource, ex);
        }
    }
}
