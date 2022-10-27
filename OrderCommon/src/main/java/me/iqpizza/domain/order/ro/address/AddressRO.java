package me.iqpizza.domain.order.ro.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import me.iqpizza.domain.order.entity.ShippingAddress;

@Getter
public class AddressRO {

    @JsonProperty("zip_code")
    private final String zipCode;

    private final String recipient;

    public AddressRO(ShippingAddress address) {
        this.zipCode = address.getZipCode();
        this.recipient = address.getRecipient();
    }
}
