package eu.activageproject.tool.repository;

import eu.activageproject.tool.domain.Database;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Database entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DatabaseRepository extends MongoRepository<Database, String> {

}
