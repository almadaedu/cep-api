package com.linx.correios.service;

import com.linx.correios.exception.NoContentException;
import com.linx.correios.exception.NotReadyException;
import com.linx.correios.model.Address;
import com.linx.correios.model.AddressStatus;
import com.linx.correios.model.enums.Status;
import com.linx.correios.repository.AddressRepository;
import com.linx.correios.repository.AddressStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressStatusRepository addressStatusRepository;


    public Status getStatus() {
        return addressStatusRepository.findById(AddressStatus.DEFAULT_ID)
                .orElse(AddressStatus.builder().status(Status.NEED_SETUP).build())
                .getStatus();
    }

    public Address getAddressByZipcode(String zipcode) throws NoContentException, NotReadyException {
        if(!getStatus().equals(Status.READY)) {
            throw new NotReadyException();
        }
        return addressRepository.findById(zipcode)
                .orElseThrow(NoContentException::new);
    }
}
