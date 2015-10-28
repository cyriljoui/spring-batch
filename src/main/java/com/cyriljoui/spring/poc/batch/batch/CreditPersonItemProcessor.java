package com.cyriljoui.spring.poc.batch.batch;

import com.cyriljoui.spring.poc.batch.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CreditPersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(CreditPersonItemProcessor.class);

    @Override
    public Person process(final Person person) throws Exception {
        log.info("Credit person ...");

        return person;
    }

}
