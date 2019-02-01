package eu.activageproject.tool.service;

import eu.activageproject.tool.domain.Database;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Database.
 */
public interface DatabaseService {

    /**
     * Save a database.
     *
     * @param database the entity to save
     * @return the persisted entity
     */
    Database save(Database database);

    /**
     * Get all the databases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Database> findAll(Pageable pageable);

    /**
     * Get the "id" database.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Database findOne(String id);

    /**
     * Delete the "id" database.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the database corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Database> search(String query, Pageable pageable);
}
