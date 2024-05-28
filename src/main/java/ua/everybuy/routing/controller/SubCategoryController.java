package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @CrossOrigin(origins = "*")
    public ResponseEntity<StatusResponse> getAllSubcategory() {
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(subCategories)
                .build());
    }
}
