package hello.typeconverter.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
public class FormatterController {
    @GetMapping("/formatter/edit")
    public Form formmaterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocaleDateTime(LocalDateTime.now());
        log.info("form={}",form);
        return form;
    }

    @PostMapping("/formatter/edit")
    public Form formatterEdit(@RequestBody Form form){
        return form;
    }

    @Data
    static class Form {
        @NumberFormat(pattern = "###,###")
        private Integer number;

        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localeDateTime;
    }
}
