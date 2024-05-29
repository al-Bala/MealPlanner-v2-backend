package com.mealplannerv2.product;

import com.mealplannerv2.repository.IngredientDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.mealplannerv2.product.ProductKeeperFacade.productsInUse;

@Log4j2
@Service
public class ProductGrouper {

    public static List<IngredientDto> getAllWithGivenWaitingLevel(long waitingLevel) {
        if (productsInUse.isEmpty()) {
            return null;
        } else {
            List<IngredientDto> listOfProductsInUse = new ArrayList<>(productsInUse);
            List<IngredientDto> result = new ArrayList<>();
            int size = listOfProductsInUse.size();

            if (waitingLevel < 0) {
                waitingLevel = 1;
                log.warn("Too small waiting level. Minimum value (1) returned.");
            }
            for (int i = 0; i < size - 1; i++) {
                LocalDate currentDate = listOfProductsInUse.get(i).getOpenDate();
                LocalDate nextDate = listOfProductsInUse.get(i + 1).getOpenDate();

                long currentDaysSinceOpening = currentDate.until(LocalDate.now(), ChronoUnit.DAYS);
                long nextDaysSinceOpening = nextDate.until(LocalDate.now(), ChronoUnit.DAYS);

                result.add(listOfProductsInUse.get(i));
                if (currentDaysSinceOpening != nextDaysSinceOpening) {
                    waitingLevel--;
                    if (waitingLevel == 0) break;
                }
            }
            if (waitingLevel != 0) {
                result.add(listOfProductsInUse.get(size - 1));
            }
            return result;
        }
    }

}
