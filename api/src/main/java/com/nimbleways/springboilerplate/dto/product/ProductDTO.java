package com.nimbleways.springboilerplate.dto.product;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private Integer leadTime;
    private Integer available;
    private String type;
    private String name;
    private LocalDate expiryDate;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
}
