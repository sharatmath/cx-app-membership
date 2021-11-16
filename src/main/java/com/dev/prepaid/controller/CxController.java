package com.dev.prepaid.controller;

import com.dev.prepaid.domain.PrepaidMaCreditOffer;
import com.dev.prepaid.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/cx")
public class CxController {

    @Autowired
    private OfferService offerService;

    @GetMapping("find-all")
    public List<PrepaidMaCreditOffer> findAll(){
        return offerService.listMaOfferBucket();
    }

    @GetMapping("/find/{id}")
    public PrepaidMaCreditOffer findById(Long id){
        return offerService.getMaCreditOfferById(id);
    }



}
