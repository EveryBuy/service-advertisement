package ua.everybuy.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;

import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class FilterService {
//
//    private List<Advertisement> advertisementList;
//    private final AdvertisementService advertisementService;
//
//    public List<Advertisement> doFilter(Double price, Integer cityId, Long subCategoryId) {
//        advertisementList = advertisementService.findAll();
//        filterPrice(price);
//        filterCity(cityId);
//        filterSubcategory(subCategoryId);
//        return advertisementList;
//    }
//
//    private void filterPrice(Double price) {
//        if (price != null) {
//            advertisementList = advertisementList.stream().filter(ad -> ad.getPrice() <= price).toList();
//        }
//    }
//
//    private void filterCity(Integer cityId) {
//        if (cityId != null) {
//            advertisementList = advertisementList.stream().filter(ad -> ad.getCity().getId() == cityId).toList();
//        }
//
//    }
//
//    private void filterSubcategory(Long subCategoryId) {
//        if (subCategoryId != null) {
//            advertisementList = advertisementList.stream().filter(ad -> ad.getSubCategory().getId() == subCategoryId).toList();
//        }
//
//    }
//}
