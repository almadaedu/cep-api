package com.linx.correios.repository;

import com.linx.correios.model.AddressStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressStatusRepository extends CrudRepository<AddressStatus, Integer> {
}
