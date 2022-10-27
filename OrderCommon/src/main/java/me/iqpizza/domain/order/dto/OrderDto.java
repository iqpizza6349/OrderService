package me.iqpizza.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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
    @Size(min = 6, max = 11)
    @JsonProperty("zip_code")
    private String zipCode;

    @Valid
    @NotEmpty
    @Size(min = 1, max = 255)
    private Set<ItemDto> items;

    @Valid
    @NotNull
    @JsonProperty("payment")
    private PaymentDto paymentDto;

}
