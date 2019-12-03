package cz.rudypokorny.zonkychallenge.configuration;

import cz.rudypokorny.zonkychallenge.zonkyapi.RestApiErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "cz.rudypokorny.zonkychallenge.loan.repository")
public class ZonkyChallengeConfiguration {

    /**
     * Teach MongoDB how to convert {@link Date} and {@link ZonedDateTime}
     */
    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter> converters = Arrays.asList(new ZonedDateTimeReadConverter(), new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }

    /**
     * Representing time service for centralized time access
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().errorHandler(new RestApiErrorHandler()).build();
    }

    /**
     * Converter for transforming {@link Date} without timezone to {@link ZonedDateTime}. UTC is used as default timezone
     */
    class ZonedDateTimeReadConverter implements Converter<Date, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(Date source) {
            return source.toInstant().atZone(ZoneOffset.UTC);
        }
    }

    /**
     * Converter for transforming {@link ZonedDateTime} to {@link Date}
     */
    class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, Date> {
        @Override
        public Date convert(ZonedDateTime source) {
            return Date.from(source.toInstant());
        }
    }

}
