package com.mealplannerv2.plangenerator.recipefilter.error;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.CreateDayResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class NotFoundMatchingRecipeErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundMatchingRecipeException.class)
    @ResponseBody
    public CreateDayResponse handleNotFoundMatchingRecipeException() {
        log.error("Not found matching recipe.");
        return new CreateDayResponse("Not found matching recipe", null);
    }
}
