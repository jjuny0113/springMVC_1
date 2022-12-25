package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.errorHandler.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
    private final MessageSource messageSource;

    @GetMapping
    @ResponseBody
    public List<Item> items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return items;
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated(SaveCheck.class) @RequestBody Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws BindException {

//        itemValidator.validate(item,bindingResult);

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("globalError", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if (bindingResult.hasErrors()) {
            log.info("isEmpty errors={}", bindingResult);

            throw new BindException(bindingResult);
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "ok";
    }

    @GetMapping("/{itemId}/edit")
    @ResponseBody
    public Item editForm(@PathVariable Long itemId, Model model) throws BindException {

        Item findItem = itemRepository.findById(itemId);

        return findItem;
    }
//    @Validated @ModelAttribute Item item, BindingResult bindingResult
    @PostMapping("/{itemId}/edit")
    @ResponseBody
    public String edit(@PathVariable Long itemId, @Validated(value = UpdateCheck.class) @RequestBody Item item, BindingResult bindingResult) throws BindException {
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("globalError", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if (bindingResult.hasErrors()) {
            log.info("isEmpty errors={}", bindingResult);

            throw new BindException(bindingResult);
        }
        itemRepository.update(itemId, item);
        return "ok";
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationResult handleBindException(BindException bindException, Locale locale) {
        return ValidationResult.create(bindException, messageSource, locale);
    }

}

