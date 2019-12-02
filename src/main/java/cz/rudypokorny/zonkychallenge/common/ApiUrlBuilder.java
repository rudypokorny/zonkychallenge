package cz.rudypokorny.zonkychallenge.common;

import cz.rudypokorny.zonkychallenge.zonkyapi.configuration.ZonkyCustomProperties;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder constructing the URL from the provided {@link ZonkyCustomProperties}.
 */
public class ApiUrlBuilder {

    private final UriComponentsBuilder builder;
    private final Map<String, String> queryParams = new HashMap<>();

    private ApiUrlBuilder(UriComponentsBuilder builder, Map<String, String> params) {
        this.builder = Objects.requireNonNull(builder);
        this.queryParams.putAll(Objects.requireNonNull(params));
    }

    public static ApiUrlBuilder create(final String url) {
        return new ApiUrlBuilder(UriComponentsBuilder.fromHttpUrl(url), new HashMap<>());
    }

    public static ApiUrlBuilder create(final String url, final Map<String, String> params) {
        return new ApiUrlBuilder(UriComponentsBuilder.fromHttpUrl(url), params);
    }

    public ApiUrlBuilder addDateQueryParam(final String key, final ZonedDateTime zonedDateTime) {
        queryParams.put(key, formatZonedDateTime(zonedDateTime));
        return this;
    }

    private UriComponentsBuilder build() {
        queryParams.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e ->
                builder.queryParam(e.getKey(), e.getValue())
        );
        return builder;
    }

    public URI buildToURI() {
        return build().build(new HashMap<>());
    }

    public String buildToString() {
        return build().toUriString();
    }

    private String formatZonedDateTime(final ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
