package com.linx.correios.controller;

import com.linx.correios.exception.NoContentException;
import com.linx.correios.exception.NotReadyException;
import com.linx.correios.model.Address;
import com.linx.correios.service.CorreiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorreiosController {

    @Autowired
    private CorreiosService correiosService;

    @GetMapping("/status")
    public String getStatus() {
        return "Service Status: " + correiosService.getStatus();
    }

    @GetMapping("/zipcode/{zipcode}")
    public Address getAdrressByZipCode(@PathVariable("zipcode") String zipCode) throws NoContentException, NotReadyException {
        return correiosService.getAddressByZipcode(zipCode);

    }
}
