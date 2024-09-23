package ua.everybuy.routing.dto.mapper.helper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotoMappingHelper {
    private final PhotoService photoService;

    @Named("getPhotoUrls")
    public List<String> getPhotoUrls(Advertisement advertisement) {
        return photoService.getPhotoUrlsByAdvertisementId(advertisement);
    }
}

