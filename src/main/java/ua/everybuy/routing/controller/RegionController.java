package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.service.location.CityService;
import ua.everybuy.service.location.RegionService;
import ua.everybuy.database.entity.City;
import ua.everybuy.database.entity.Region;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/region")
public class RegionController {
    private final RegionService regionService;
    private final CityService cityService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Region> getAllRegions() {
        return regionService.getAllRegions();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/{regionId}/cities"})
    public List<City> getAllCitiesByRegionId(@PathVariable Long regionId) {
        return cityService.getCitiesByRegionId(regionId);
    }
}
