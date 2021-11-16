package com.dev.prepaid.controller;

import com.dev.prepaid.domain.PrepaidMaCreditOffer;
import com.dev.prepaid.model.create.CreateResponse;
import com.dev.prepaid.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "create-ma")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createMaCredit(@RequestBody  List<Map<String, Object>> payload){
        log.info("{}", payload);
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaCredit(@RequestBody String body){
        log.info("delete MA Credit");

    }

}