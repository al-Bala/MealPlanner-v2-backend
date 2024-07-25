package com.mealplannerv2.product.infrastructure.controller;

import com.mealplannerv2.product.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController
public class ProductController {

    private final ProductFacade productFacade;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;

    @GetMapping("/product/{id}")
    public ResponseEntity<WeightResponse> getStandardWeightById(@PathVariable("id") String id) {
        WeightResponse weightResponse = productFacade.getStandardProductWeight(id);
        return ResponseEntity.ok(weightResponse);
    }

    @GetMapping("/products")
    public List<ProductResponse> getProducts(@RequestParam String query) {
        if(query.isEmpty()){
            return Collections.emptyList();
        }
        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);

        return products.stream()
                .map(p -> ProductResponse.builder()
                        .id(p.id())
                        .name(p.name())
                        .packingUnits(findUnits(p.packing_units()))
//                        .packingUnits(p.packing_units())
                        .mainUnit(p.main_unit())
                        .standardWeight(p.standard_weight())
                        .build())
                .limit(3)
                .toList();
    }

    private List<String> findUnits(List<String> labels){
        List<String> allUnits = new ArrayList<>();
        labels.forEach(pu -> convertUnits(pu, allUnits));
        return allUnits;
    }

    private void convertUnits(String label, List<String> list){
        Unit unitByLabel = unitRepository.findByLabel(label);
        list.addAll(unitByLabel.units());
    }

//    @GetMapping("/products")
//    public List<ProductResponse> getProducts(@RequestParam String query) {
//        if(query.isEmpty()){
//            return Collections.emptyList();
//        }
//        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);
//        return products.stream()
//                .map(p -> ProductResponse.builder()
//                        .id(p.id())
//                        .name(p.name())
//                        .packingUnits(p.packing_units())
//                        .mainUnit(p.main_unit())
//                        .standardWeight(p.standard_weight())
//                        .build())
//                .toList();
//    }

}
