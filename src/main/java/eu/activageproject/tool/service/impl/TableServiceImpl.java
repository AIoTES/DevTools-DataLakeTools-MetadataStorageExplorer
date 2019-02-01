package eu.activageproject.tool.service.impl;

import eu.activageproject.tool.service.TableService;
import eu.activageproject.tool.domain.Table;
import eu.activageproject.tool.repository.TableRepository;
import eu.activageproject.tool.repository.search.TableSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Table.
 */
@Service
public class TableServiceImpl implements TableService {

    private final Logger log = LoggerFactory.getLogger(TableServiceImpl.class);

    private final TableRepository tableRepository;

    private final TableSearchRepository tableSearchRepository;

    public TableServiceImpl(TableRepository tableRepository, TableSearchRepository tableSearchRepository) {
        this.tableRepository = tableRepository;
        this.tableSearchRepository = tableSearchRepository;
    }

    /**
     * Save a table.
     *
     * @param table the entity to save
     * @return the persisted entity
     */
    @Override
    public Table save(Table table) {
        log.debug("Request to save Table : {}", table);
        Table result = tableRepository.save(table);
        //tableSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the tables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Table> findAll(Pageable pageable) {
        log.debug("Request to get all Tables");
        return tableRepository.findAll(pageable);
    }

    /**
     * Get one table by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Table findOne(String id) {
        log.debug("Request to get Table : {}", id);
        return tableRepository.findOne(id);
    }

    /**
     * Delete the table by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Table : {}", id);
        tableRepository.delete(id);
       // tableSearchRepository.delete(id);
    }

    /**
     * Search for the table corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Table> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Tables for query {}", query);
        Page<Table> result = tableSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
