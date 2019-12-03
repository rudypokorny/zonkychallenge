package cz.rudypokorny.zonkychallenge.loan.web;

import cz.rudypokorny.zonkychallenge.loan.web.dto.ValidationError;
import cz.rudypokorny.zonkychallenge.loan.web.dto.ValidationErrorBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Configuration
public class WebConfiguration {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ValidationError handleValidationError(final Exception exception) {
        if (exception instanceof DuplicateKeyException) {
            return ValidationErrorBuilder.forMessaget("An error has occurred during entity saving - duplicate key");
        } else if (exception instanceof HttpMessageNotReadableException) {
            return ValidationErrorBuilder.forMessaget("Invalid request - message not readable");
        }
        return ValidationErrorBuilder.forMessaget(exception.getMessage());
    }
}
