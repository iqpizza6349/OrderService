package me.iqpizza.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class OrderDto {

    @NotEmpty
    @Size(min = 2, max = 255)
    private String recipient;

    @NotEmpty
    @Size(min = 5, max = 10)
    @JsonProperty("zip_code")
    private String zipCode;

    @NotEmpty
    @Size(min = 1, max = 255)
    private Set<ItemDto> items;

    @NotNull
    @JsonProperty("payment")
    private PaymentDto paymentDto;

}
