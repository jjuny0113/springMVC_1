package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.errorHandler.ValidationResult;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
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

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;
    private final MessageSource messageSource;

    @GetMapping

    public List<Item> items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return items;
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @RequestBody ItemSaveForm item, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws BindException {

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

        Item saveItem = new Item(item.getItemName(),item.getPrice(),item.getQuantity());


        Item savedItem = itemRepository.save(saveItem);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "ok";
    }

    @GetMapping("/{itemId}/edit")

    public Item editForm(@PathVariable Long itemId, Model model) throws BindException {

        Item findItem = itemRepository.findById(itemId);

        return findItem;
    }
//    @Validated @ModelAttribute Item item, BindingResult bindingResult
    @PostMapping("/{itemId}/edit")

    public String edit(@PathVariable Long itemId, @Validated @RequestBody ItemUpdateForm item, BindingResult bindingResult) throws BindException {
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

        itemRepository.update(itemId, new Item(item.getItemName(),item.getPrice(), item.getQuantity()));
        return "ok";
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationResult handleBindException(BindException bindException, Locale locale) {
        return ValidationResult.create(bindException, messageSource, locale);
    }

}

