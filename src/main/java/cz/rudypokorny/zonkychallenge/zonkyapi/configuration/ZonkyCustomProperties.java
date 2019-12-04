package cz.rudypokorny.zonkychallenge.zonkyapi.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class serves as java configuration object wrapping the properties related to the custom area, denoted by special prefix.
 */
@Data
@Component
@ConfigurationProperties(prefix = "zonky.loans")
public class ZonkyCustomProperties {

    /**
     * Base URL to which all request are made
     */
    private String url;

    /**
     * Additional url parameters applies as query parameters to the request
     */
    private Map<String, String> urlParams = new HashMap<>();

    /**
     * Scheduler interval in milliseconds, according which the requests are made
     */
    private int refreshInterval;

    /**
     * Number of hours for which the current time should be subtracted, in case of default search
     */
    private int defaultSearchRange;

    /**
     * Default number to pe used for paging, value of X-Size header key
     */
    private String pageSize = "1000";

    /*
     Default admin credentials for the web application. Only for demo purposes.
     */
    private String adminUsername;
    private String adminPassword;

}
