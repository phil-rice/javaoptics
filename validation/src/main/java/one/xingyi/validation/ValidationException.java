package one.xingyi.validation;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeException {

    public final List<String> errors;
    public ValidationException(List<String> errors) {
        this(null, errors);
    }
    public ValidationException(String message, List<String> errors) {
        super(message == null ? errors.toString() : message);
        this.errors = Collections.unmodifiableList(errors);
    }
}
