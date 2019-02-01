package eu.activageproject.tool.service.impl;

import eu.activageproject.tool.service.QueryService;
import eu.activageproject.tool.domain.Query;
import eu.activageproject.tool.repository.QueryRepository;
import eu.activageproject.tool.repository.search.QuerySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Query.
 */
@Service
public class QueryServiceImpl implements QueryService {

    private final Logger log = LoggerFactory.getLogger(QueryServiceImpl.class);

    private final QueryRepository queryRepository;

    private final QuerySearchRepository querySearchRepository;

    public QueryServiceImpl(QueryRepository queryRepository, QuerySearchRepository querySearchRepository) {
        this.queryRepository = queryRepository;
        this.querySearchRepository = querySearchRepository;
    }

    /**
     * Save a query.
     *
     * @param query the entity to save
     * @return the persisted entity
     */
    @Override
    public Query save(Query query) {
        log.debug("Request to save Query : {}", query);
        Query result = queryRepository.save(query);
        querySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the queries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Query> findAll(Pageable pageable) {
        log.debug("Request to get all Queries");
        return queryRepository.findAll(pageable);
    }

    /**
     * Get one query by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Query findOne(String id) {
        log.debug("Request to get Query : {}", id);
        return queryRepository.findOne(id);
    }

    /**
     * Delete the query by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Query : {}", id);
        queryRepository.delete(id);
        querySearchRepository.delete(id);
    }

    /**
     * Search for the query corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Query> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Queries for query {}", query);
        Page<Query> result = querySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
