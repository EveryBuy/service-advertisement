package ua.everybuy.routing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceRangeDto {
    private Double minPrice;
    private Double maxPrice;
}
