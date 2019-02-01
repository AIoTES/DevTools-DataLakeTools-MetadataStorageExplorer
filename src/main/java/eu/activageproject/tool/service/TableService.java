package eu.activageproject.tool.service;

import eu.activageproject.tool.domain.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Table.
 */
public interface TableService {

    /**
     * Save a table.
     *
     * @param table the entity to save
     * @return the persisted entity
     */
    Table save(Table table);

    /**
     * Get all the tables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Table> findAll(Pageable pageable);

    /**
     * Get the "id" table.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Table findOne(String id);

    /**
     * Delete the "id" table.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the table corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Table> search(String query, Pageable pageable);
}
