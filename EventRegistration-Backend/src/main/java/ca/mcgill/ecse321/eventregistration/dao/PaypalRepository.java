package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Paypal;

public interface PaypalRepository extends CrudRepository<Paypal, Integer> {

    Paypal findByPaypalId(int Id);

    Paypal findByEmail(String email);
}