package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.City;
import ua.everybuy.database.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> getAllCities (){
        return cityRepository.findAll();
    }
    public City findById(Long id){
       return cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("City not found"));
    }

}
