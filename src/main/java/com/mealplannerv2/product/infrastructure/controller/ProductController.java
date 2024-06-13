package com.mealplannerv2.product.infrastructure.controller;

import com.mealplannerv2.product.ProductFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping("/product/{id}")
    public ResponseEntity<WeightResponse> getStandardWeightById(@PathVariable("id") String id) {
        WeightResponse weightResponse = productFacade.getStandardProductWeight(id);
        return ResponseEntity.ok(weightResponse);
    }

}
