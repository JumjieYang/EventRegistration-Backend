package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Performer;

public interface PerformerRepository extends CrudRepository<Performer, String> {
    
    Performer findByName(String name);
}
