package com.mealplannerv2.storage.leftovers;

import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
@Component
public class LeftoversFacade {

    private static final int ONE_DAY = 1;
    private final LeftoversService leftoversService;
    private final LeftoversGrouper leftoversGrouper;
    private final LeftoversMapper mapper;

    public Map<String, LeftoverDto> getLeftovers(){
        return leftoversService.getLeftovers();
    }

//    public void addToStoredProducts(StoredProductDto2 newProduct){
//        storedProductsService.addUniq(newProduct);
//    }

    public void addAllToLeftovers(List<LeftoverDto> leftovers){
        leftoversService.addAllUniq(leftovers);
    }

    public void convertAndAddToLeftovers(List<IngredientDto> ingsDto) {
        List<LeftoverDto> converted = convertIntoLeftovers(ingsDto);
        addAllToLeftovers(converted);
    }

    public List<IngredientDto> removeUsedLeftovers(List<IngredientDto> ingsDto){
        return leftoversService.remove(ingsDto);
    }

    public void updateSpoilDatesForLeftovers(){
        leftoversService.updateDaysToSpoilAfterOpening();
    }

    public List<LeftoverDto> convertIntoLeftovers(List<IngredientDto> ingsDto){
        return mapper.mapFromIngredientsDtoToLeftovers(ingsDto);
    }

    public List<LeftoverDto> getLeftoversWhichMustBeUsedFirstly() {
        if (getLeftovers().isEmpty()) {
            log.warn("Leftovers map is empty.");
            return null;
        } else {
            return leftoversGrouper.getLeftoversWithTheFewestDaysToSpoil(ONE_DAY);
        }
    }
}
