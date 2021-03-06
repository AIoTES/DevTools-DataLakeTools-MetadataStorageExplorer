package eu.activageproject.tool.service;

import eu.activageproject.tool.domain.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Model.
 */
public interface ModelService {

    /**
     * Save a model.
     *
     * @param model the entity to save
     * @return the persisted entity
     */
    Model save(Model model);

    /**
     * Get all the models.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Model> findAll(Pageable pageable);

    /**
     * Get the "id" model.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Model findOne(String id);

    /**
     * Delete the "id" model.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the model corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Model> search(String query, Pageable pageable);
}
