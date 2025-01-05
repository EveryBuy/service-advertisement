package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.buisnesslogic.service.location.RegionService;
import ua.everybuy.database.entity.Region;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("ad/region")
public class RegionController {
    private final RegionService regionService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Region> getAllRegions() {
        return regionService.getAllRegions();
    }
}
