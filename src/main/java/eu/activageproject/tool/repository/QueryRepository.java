package eu.activageproject.tool.repository;

import eu.activageproject.tool.domain.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Query entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueryRepository extends MongoRepository<Query, String> {

}
