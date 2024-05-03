package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.AdvertisementPhotoService;
import ua.everybuy.database.entity.AdvertisementPhoto;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementPhotoService photoService;

    @PostMapping("/photo")
    public ResponseEntity<?> createAdvertisement(
            @RequestParam("photos") MultipartFile[] photos,
            @RequestParam ("id") Long advertisementId
            ) {

        try {
            List<AdvertisementPhoto> uploadedPhotos = photoService.handlePhotoUpload(photos, advertisementId);
            return ResponseEntity.ok(uploadedPhotos);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error uploading photos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating advertisement: " + e.getMessage());
        }
    }

}





