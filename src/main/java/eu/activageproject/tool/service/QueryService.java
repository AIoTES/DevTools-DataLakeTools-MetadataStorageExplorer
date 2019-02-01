package eu.activageproject.tool.service;

import eu.activageproject.tool.domain.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Query.
 */
public interface QueryService {

    /**
     * Save a query.
     *
     * @param query the entity to save
     * @return the persisted entity
     */
    Query save(Query query);

    /**
     * Get all the queries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Query> findAll(Pageable pageable);

    /**
     * Get the "id" query.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Query findOne(String id);

    /**
     * Delete the "id" query.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the query corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Query> search(String query, Pageable pageable);
}
