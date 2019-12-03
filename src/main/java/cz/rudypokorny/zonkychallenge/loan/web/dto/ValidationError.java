package cz.rudypokorny.zonkychallenge.loan.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<String> validationMessages = new ArrayList<>();
    private final String errorMessage;

    ValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addValidationMessage(final String validationMessage) {
        this.validationMessages.add(validationMessage);
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
