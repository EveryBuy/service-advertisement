package ua.everybuy.routing.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.everybuy.routing.dto.util.PriceSerializer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceRangeDto {
    @JsonSerialize(using = PriceSerializer.class)
    private Double minPrice;
    @JsonSerialize(using = PriceSerializer.class)
    private Double maxPrice;
}
