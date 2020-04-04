package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Theatre;

public interface TheatreRepository extends CrudRepository<Theatre, String>{

	Theatre findByName(String name);

}