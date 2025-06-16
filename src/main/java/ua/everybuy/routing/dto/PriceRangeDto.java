package ua.everybuy.routing.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import ua.everybuy.routing.dto.util.PriceSerializer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceRangeDto {
    @JsonSerialize(using = PriceSerializer.class)
    private Double minPrice;
    @JsonSerialize(using = PriceSerializer.class)
    private Double maxPrice;
}
