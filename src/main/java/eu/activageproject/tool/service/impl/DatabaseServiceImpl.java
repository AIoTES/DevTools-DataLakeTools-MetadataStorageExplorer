package eu.activageproject.tool.service.impl;

import eu.activageproject.tool.service.DatabaseService;
import eu.activageproject.tool.domain.Database;
import eu.activageproject.tool.repository.DatabaseRepository;
import eu.activageproject.tool.repository.search.DatabaseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Database.
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {

    private final Logger log = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    private final DatabaseRepository databaseRepository;

    private final DatabaseSearchRepository databaseSearchRepository;

    public DatabaseServiceImpl(DatabaseRepository databaseRepository, DatabaseSearchRepository databaseSearchRepository) {
        this.databaseRepository = databaseRepository;
        this.databaseSearchRepository = databaseSearchRepository;
    }

    /**
     * Save a database.
     *
     * @param database the entity to save
     * @return the persisted entity
     */
    @Override
    public Database save(Database database) {
        log.debug("Request to save Database : {}", database);
        Database result = databaseRepository.save(database);
        databaseSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the databases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Database> findAll(Pageable pageable) {
        log.debug("Request to get all Databases");
        return databaseRepository.findAll(pageable);
    }

    /**
     * Get one database by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Database findOne(String id) {
        log.debug("Request to get Database : {}", id);
        return databaseRepository.findOne(id);
    }

    /**
     * Delete the database by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Database : {}", id);
        databaseRepository.delete(id);
        databaseSearchRepository.delete(id);
    }

    /**
     * Search for the database corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Database> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Databases for query {}", query);
        Page<Database> result = databaseSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
