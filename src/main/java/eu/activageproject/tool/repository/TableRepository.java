package eu.activageproject.tool.repository;

import eu.activageproject.tool.domain.Table;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Table entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TableRepository extends MongoRepository<Table, String> {

}
