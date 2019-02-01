package eu.activageproject.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.activageproject.tool.domain.Query;
import eu.activageproject.tool.service.QueryService;
import eu.activageproject.tool.web.rest.errors.BadRequestAlertException;
import eu.activageproject.tool.web.rest.util.HeaderUtil;
import eu.activageproject.tool.web.rest.util.PaginationUtil;
import io.github.simlife.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Query.
 */
@RestController
@RequestMapping("/api")
public class QueryResource {

    private final Logger log = LoggerFactory.getLogger(QueryResource.class);

    private static final String ENTITY_NAME = "query";

    private final QueryService queryService;

    public QueryResource(QueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * POST  /queries : Create a new query.
     *
     * @param query the query to create
     * @return the ResponseEntity with status 201 (Created) and with body the new query, or with status 400 (Bad Request) if the query has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/queries")
    @Timed
    public ResponseEntity<Query> createQuery(@Valid @RequestBody Query query) throws URISyntaxException {
        log.debug("REST request to save Query : {}", query);
        if (query.getId() != null) {
            throw new BadRequestAlertException("A new query cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Query result = queryService.save(query);
        return ResponseEntity.created(new URI("/api/queries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /queries : Updates an existing query.
     *
     * @param query the query to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated query,
     * or with status 400 (Bad Request) if the query is not valid,
     * or with status 500 (Internal Server Error) if the query couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/queries")
    @Timed
    public ResponseEntity<Query> updateQuery(@Valid @RequestBody Query query) throws URISyntaxException {
        log.debug("REST request to update Query : {}", query);
        if (query.getId() == null) {
            return createQuery(query);
        }
        Query result = queryService.save(query);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, query.getId().toString()))
            .body(result);
    }

    /**
     * GET  /queries : get all the queries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of queries in body
     */
    @GetMapping("/queries")
    @Timed
    public ResponseEntity<List<Query>> getAllQueries(Pageable pageable) {
        log.debug("REST request to get a page of Queries");
        Page<Query> page = queryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/queries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /queries/:id : get the "id" query.
     *
     * @param id the id of the query to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the query, or with status 404 (Not Found)
     */
    @GetMapping("/queries/{id}")
    @Timed
    public ResponseEntity<Query> getQuery(@PathVariable String id) {
        log.debug("REST request to get Query : {}", id);
        Query query = queryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(query));
    }

    /**
     * DELETE  /queries/:id : delete the "id" query.
     *
     * @param id the id of the query to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/queries/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuery(@PathVariable String id) {
        log.debug("REST request to delete Query : {}", id);
        queryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/queries?query=:query : search for the query corresponding
     * to the query.
     *
     * @param query the query of the query search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/queries")
    @Timed
    public ResponseEntity<List<Query>> searchQueries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Queries for query {}", query);
        Page<Query> page = queryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/queries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
