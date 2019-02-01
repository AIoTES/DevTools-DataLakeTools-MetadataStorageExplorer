package eu.activageproject.tool.repository;

import eu.activageproject.tool.domain.Model;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Model entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelRepository extends MongoRepository<Model, String> {

}
