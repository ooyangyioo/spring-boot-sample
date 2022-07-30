//package org.yangyi.project.config;
//
//import org.springframework.beans.factory.config.PropertiesFactoryBean;
//import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import java.io.IOException;
//
//@Configuration
//public class QuartzConfig implements SchedulerFactoryBeanCustomizer {
//
//    @Override
//    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
//        schedulerFactoryBean.setStartupDelay(2);
//        schedulerFactoryBean.setAutoStartup(true);
//        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
//        schedulerFactoryBean.setOverwriteExistingJobs(true);
//    }
//
//    @Bean
//    @Profile({"mysql", "default"})
//    public SchedulerFactoryBean mySQLSchedulerFactoryBean() throws IOException {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz-mysql.properties"));
//        propertiesFactoryBean.afterPropertiesSet();
//        schedulerFactoryBean.setQuartzProperties(propertiesFactoryBean.getObject());
//        return schedulerFactoryBean;
//    }
//
//    @Bean
//    @Profile("postgres")
//    public SchedulerFactoryBean postgresSchedulerFactoryBean() throws IOException {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz-postgres.properties"));
//        propertiesFactoryBean.afterPropertiesSet();
//        schedulerFactoryBean.setQuartzProperties(propertiesFactoryBean.getObject());
//        return schedulerFactoryBean;
//    }
//
//}
