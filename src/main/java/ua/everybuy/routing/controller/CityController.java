package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.CityService;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.StatusResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("ad/city")
public class CityController {
    private final CityService cityService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public ResponseEntity<StatusResponse> getAllCities() {
        List<City> cityList = cityService.getAllCities();

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(cityList)
                .build());
    }
}
