package eu.activageproject.tool.service;

import eu.activageproject.tool.domain.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Schema.
 */
public interface SchemaService {

    /**
     * Save a schema.
     *
     * @param schema the entity to save
     * @return the persisted entity
     */
    Schema save(Schema schema);

    /**
     * Get all the schemata.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Schema> findAll(Pageable pageable);

    /**
     * Get the "id" schema.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Schema findOne(String id);

    /**
     * Delete the "id" schema.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the schema corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Schema> search(String query, Pageable pageable);
}
