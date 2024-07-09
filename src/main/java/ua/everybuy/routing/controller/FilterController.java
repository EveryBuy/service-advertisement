package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.everybuy.buisnesslogic.service.FilterService;

import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.database.entity.Advertisement;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad")
public class FilterController {
    private final FilterService filterService;

    @GetMapping("/filter")
    public List<Advertisement> doFilter(@RequestParam(required = false) Double price,
                                        @RequestParam(required = false) Long cityId,
                                        @RequestParam(required = false) Long subCategoryId,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) String productType){
        return filterService.doFilter(price, cityId, subCategoryId, categoryId, productType);
    }
}
