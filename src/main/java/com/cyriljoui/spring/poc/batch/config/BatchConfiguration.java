package com.cyriljoui.spring.poc.batch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Use a custom datasource for Spring Batch.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Bean
    public BatchDatabaseInitializer batchDatabaseInitializer(final BatchProperties properties,
                                                             @Qualifier("batchDataSource") final DataSource dataSource,
                                                             final ResourceLoader resourceLoader) {
        // Only for POC
        // Batch initialize Batch DB schema (in production you must not use that)
        BatchDatabaseInitializer batchDatabaseInitializer = new BatchDatabaseInitializer() {
            @PostConstruct
            protected void initialize() {
                if(properties.getInitializer().isEnabled()) {
                    String platform = null;
                    try {
                        platform = DatabaseType.fromMetaData(dataSource).toString().toLowerCase();
                    } catch (MetaDataAccessException e) {
                        e.printStackTrace();
                    }
                    if("hsql".equals(platform)) {
                        platform = "hsqldb";
                    }

                    if("postgres".equals(platform)) {
                        platform = "postgresql";
                    }

                    if("oracle".equals(platform)) {
                        platform = "oracle10g";
                    }

                    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                    String schemaLocation = properties.getSchema();
                    schemaLocation = schemaLocation.replace("@@platform@@", platform);
                    populator.addScript(resourceLoader.getResource(schemaLocation));
                    populator.setContinueOnError(true);
                    DatabasePopulatorUtils.execute(populator, dataSource);
                }

            }
        };
        return batchDatabaseInitializer;
    }

    @Override
    @Autowired
    public void setDataSource(@Qualifier("batchDataSource") DataSource batchDataSource) {
        super.setDataSource(batchDataSource);
    }
}
