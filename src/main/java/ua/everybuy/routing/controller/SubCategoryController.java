package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.SubCategoryService;
import ua.everybuy.database.entity.SubCategory;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ad/subcategory")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public StatusResponse<List<SubCategory>> getAllSubcategory() {
        List<SubCategory> subCategoryList = subCategoryService.getAllSubCategories();
        return new StatusResponse<>(HttpStatus.OK.value(), subCategoryList);
    }
}
