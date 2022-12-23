package hello.itemservice.web.errorHandler;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FieldErrorDetail {
    private String field;


    private String message;

    public static FieldErrorDetail create(ObjectError error, MessageSource messageSource, Locale locale) {
        return new FieldErrorDetail(
                error.getCode(),
                messageSource.getMessage(error, locale));
    }
}
