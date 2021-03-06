package com.cyriljoui.spring.poc.batch.batch;

import com.cyriljoui.spring.poc.batch.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DisablePersonProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(DisablePersonProcessor.class);

    @Override
    public Person process(final Person person) throws Exception {
        log.info("Disable person ..." + person);

        return person;
    }

}
