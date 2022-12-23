package hello.itemservice.web.errorHandler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationResult {
    private List<FieldErrorDetail> errors;

    public static ValidationResult create(Errors errors, MessageSource messageSource, Locale locale) {
        List<FieldErrorDetail> details = errors.getAllErrors()
                .stream()
                .map(error -> FieldErrorDetail.create(
                        error,
                        messageSource,
                        locale)
                )
                .collect(Collectors.toList());


        return new ValidationResult(details);
    }


}
