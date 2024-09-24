package ua.everybuy.routing.dto.mapper.helper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class DescriptionHelper {

    @Named("truncateDescription")
    public String truncateDescription(String description) {
        if (description != null && description.length() > 200) {
            return description.substring(0, 200);
        }
        return description;
    }
}
