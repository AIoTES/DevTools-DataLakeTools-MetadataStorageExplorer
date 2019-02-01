package eu.activageproject.tool.repository;

import eu.activageproject.tool.domain.Schema;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Schema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchemaRepository extends MongoRepository<Schema, String> {

}
