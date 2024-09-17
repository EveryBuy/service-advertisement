package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.location.CityService;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.response.StatusResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("ad/city")
public class CityController {
    private final CityService cityService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public StatusResponse<List<City>> getAllCities() {
        List<City> cityList = cityService.getAllCities();
        return new StatusResponse<>(HttpStatus.OK.value(), cityList);
    }
}
