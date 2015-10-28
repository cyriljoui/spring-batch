package com.cyriljoui.spring.poc.batch.batch;

import com.cyriljoui.spring.poc.batch.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class BatchDisablePersonConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BatchDisablePersonConfiguration.class);

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<Person> readerPersonDb(DataSource dataSource) {
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<Person>();
        reader.setRowMapper(new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = new Person();
                person.setFirstName(rs.getString("first_name"));
                person.setLastName(rs.getString("last_name"));
                return person;
            }
        });
        reader.setSql("SELECT * FROM people");
        reader.setDataSource(dataSource);
        return reader;
    }

    @Bean
    public ItemWriter<Person> writerExportDisabled() {
        Resource resource = new FileSystemResource("target/export-disabled.txt");
        log.info("Export resource: " + resource);
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<Person>();
        writer.setResource(resource);
        writer.setLineAggregator(new LineAggregator<Person>() {
            @Override
            public String aggregate(Person item) {
                log.info("Export disable person: " + item);
                return item.getFirstName() + '-' + item.getLastName();
            }
        });
        return writer;
    }

    @Bean
    public DisablePersonItemProcessor disablePersonItemProcessor() {
        return new DisablePersonItemProcessor();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job disablePerson(JobBuilderFactory jobs, Step stepDisablePerson) {
        return jobs.get("disablePersonJob")
                .incrementer(new RunIdIncrementer())
                .flow(stepDisablePerson)
                .end()
                .build();
    }

    @Bean
    public Step stepDisablePerson(StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        return stepBuilderFactory.get("step-disable")
                .<Person, Person> chunk(10)
                .reader(readerPersonDb(dataSource))
                .processor(disablePersonItemProcessor())
                .writer(writerExportDisabled())
                .build();
    }

    // end::jobstep[]
}
