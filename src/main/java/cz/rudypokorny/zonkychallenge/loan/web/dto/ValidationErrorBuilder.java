package cz.rudypokorny.zonkychallenge.loan.web.dto;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationErrorBuilder {

    public static ValidationError fromBindingErrors(final Errors errors) {
        ValidationError validationError = new ValidationError(String.format("Validation failed with %s errors. ", +errors.getErrorCount()));
        errors.getAllErrors().stream().map(ValidationErrorBuilder::extractValidationMessage)
                .forEach(m -> validationError.addValidationMessage(m));
        return validationError;
    }

    public static ValidationError forMessaget(final String message) {
        return new ValidationError(message);
    }

    private static String extractValidationMessage(final ObjectError objectError) {
        return ((FieldError) objectError).getField() + " " + objectError.getDefaultMessage();
    }

}
