package com.linx.correios.service;

import com.linx.correios.CorreiosApplication;
import com.linx.correios.exception.NoContentException;
import com.linx.correios.exception.NotReadyException;
import com.linx.correios.model.Address;
import com.linx.correios.model.AddressStatus;
import com.linx.correios.model.enums.Status;
import com.linx.correios.repository.AddressRepository;
import com.linx.correios.repository.AddressStatusRepository;
import com.linx.correios.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressStatusRepository addressStatusRepository;

    @Autowired
    private SetupRepository setupRepository;

    public Status getStatus() {
        return addressStatusRepository.findById(AddressStatus.DEFAULT_ID)
                .orElse(AddressStatus.builder().status(Status.NEED_SETUP).build())
                .getStatus();
    }

    public Address getAddressByZipcode(String zipcode) throws NoContentException, NotReadyException{
        if(!getStatus().equals(Status.READY)) {
            throw new NotReadyException();
        }
        return addressRepository.findById(zipcode)
                .orElseThrow(NoContentException::new);
    }

    private void saveStatus(Status status) {
        addressStatusRepository.save(AddressStatus.builder()
                .id(AddressStatus.DEFAULT_ID)
                .status(status)
                .build());
    }
    public void setup() throws Exception{
        logger.info("-----SETUP RUNNING----");
        if(getStatus().equals(Status.NEED_SETUP)) {
            saveStatus(Status.SETUP_RUNNING);
            try {
                addressRepository.saveAll(setupRepository.getfromOrigin());
            }catch (Exception exception) {
                saveStatus(Status.NEED_SETUP);
                throw exception;
            }

            saveStatus(Status.READY);
        }
        logger.info("-----SERVICE READY----");
    }

    //Comportamento do spring para não rodar de forma inútil
    @EventListener(ApplicationStartedEvent.class)
    private void setupOnStartup(){
        try {
            this.setup();
        }catch (Exception e) {
            CorreiosApplication.close(999);

        }
    }
}
