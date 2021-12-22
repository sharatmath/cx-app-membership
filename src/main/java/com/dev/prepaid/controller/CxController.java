package com.dev.prepaid.controller;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.PrepaidMaCreditOffer;
import com.dev.prepaid.model.create.CreateResponse;
import com.dev.prepaid.repository.PrepaidMaOfferBucketRepository;
import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.util.GsonUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/cx/")
public class CxController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private PrepaidMaOfferBucketRepository prepaidMaOfferBucketRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("find-all")
    public List<PrepaidMaCreditOffer> findAll(){
        return offerService.listMaOfferBucket();
    }

    @GetMapping("find/{id}")
    public PrepaidMaCreditOffer findById(Long id){
        return offerService.getMaCreditOfferById(id);
    }

    @PostMapping(value = "create-ma")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PrepaidMaCreditOffer> createMaCredit(@RequestBody  PrepaidMaCreditOffer prepaidMaCreditOffer){
        log.info("{}", prepaidMaCreditOffer);
        PrepaidMaCreditOffer result = prepaidMaOfferBucketRepository.save(prepaidMaCreditOffer);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping(value = "update-ma")
    public ResponseEntity<PrepaidMaCreditOffer> updateMaOffer(@RequestBody PrepaidMaCreditOffer prepaidMaCreditOffer) {
        log.info("update MA credit");

        PrepaidMaCreditOffer result = prepaidMaOfferBucketRepository.save(prepaidMaCreditOffer);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(value = "delete-ma")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaCredit(@RequestBody String body) throws JSONException {
        log.info("delete MA Credit");
        log.info(GsonUtils.deserializeObjectToJSON(body));

        JSONObject request = new JSONObject(body);
        Long id = request.getLong("id");

        offerService.deletePrepaidMaCreditOffer(id);
    }


}
