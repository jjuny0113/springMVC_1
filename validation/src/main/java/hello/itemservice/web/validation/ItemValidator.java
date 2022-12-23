package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        // == 보단 isAssignableFrom을 이용하면 자식클래스까지 이렇게 하면 검증 가능 
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        //        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//        비어있거나 "" 인 경우(단순 한 경우) 위처럼 할 수 있다.
        //검증 로직
        if (item.getItemName() == null || !StringUtils.hasText(item.getItemName())) {
//            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
//            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,null,null ,"상품 이름은 필수입니다."));
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, new Object[]{item.getItemName()}, null));
//            bindingResult는 item이 타겟인걸 이미 알고있다. 그래서 아래처럼 입력하면 required.item.itemName으로 에러를 찾는다.
//            bindingResult.rejectValue("itemName", "required", new Object[]{item.getItemName()}, null);
            errors.rejectValue("itemName", "required");

        }


        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000,item.getPrice()}, null));
            errors.rejectValue("price", "range", new Object[]{1000, 1000000, item.getPrice()}, null);

        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{1,item.getQuantity()}, null));

            errors.rejectValue("quantity", "max", new Object[]{9999, item.getQuantity()}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("globalError", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

    }
}
