package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.service.location.CityService;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.response.StatusResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/city")
public class CityController {
    private final CityService cityService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public StatusResponse<List<City>> getAllCities() {
        List<City> cityList = cityService.getAllCitiesWithRegions();
        return new StatusResponse<>(HttpStatus.OK.value(), cityList);
    }

    @GetMapping("/search")
    public List <City> searchCities(@RequestParam @Valid String keyword) {
        return cityService.smartSearchByName(keyword);
    }
}
