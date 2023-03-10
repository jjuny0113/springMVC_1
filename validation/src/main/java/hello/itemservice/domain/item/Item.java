package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(
//        lang="javascript",
//        script = "_this.price * _this.quantity >= 10000",
//        message = "총합이 10000원 이상이여야합니다.")
public class Item {

//    @NotNull(groups = UpdateCheck.class) // 수정 요구사항으로 추가
    private Long id;

//    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;
//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Range(min=1000,max=1000000)
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999,groups = {SaveCheck.class}) // 수정 요구사항을 추가
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
