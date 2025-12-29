package com.nimbleways.springboilerplate.dto.product;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Set<ProductDTO> items;
}
