package cz.rudypokorny.zonkychallenge.zonkyapi;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Simple client error handler - just logs an error message
 */
@Log4j2
public class RestApiErrorHandler extends DefaultResponseErrorHandler {

    /**
     * Obtains the error from the response body and passes it as ERROR into the class logger
     *
     * @param response error HTTP response
     * @throws IOException in case when either response status or response payload cannot be obtained
     */
    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        final String message = constructErrorMessage(response);
        log.error(message);
    }

    private String constructErrorMessage(final ClientHttpResponse response) throws IOException {
        final String bodyContent = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
        return String.format("Invocation resulted in failure. Response code %s: [%s]", response.getStatusCode().toString(), bodyContent);
    }
}
