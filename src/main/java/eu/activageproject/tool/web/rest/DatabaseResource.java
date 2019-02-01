package eu.activageproject.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.activageproject.tool.domain.Database;
import eu.activageproject.tool.service.DatabaseService;
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
 * REST controller for managing Database.
 */
@RestController
@RequestMapping("/api")
public class DatabaseResource {

    private final Logger log = LoggerFactory.getLogger(DatabaseResource.class);

    private static final String ENTITY_NAME = "database";

    private final DatabaseService databaseService;

    public DatabaseResource(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * POST  /databases : Create a new database.
     *
     * @param database the database to create
     * @return the ResponseEntity with status 201 (Created) and with body the new database, or with status 400 (Bad Request) if the database has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/databases")
    @Timed
    public ResponseEntity<Database> createDatabase(@Valid @RequestBody Database database) throws URISyntaxException {
        log.debug("REST request to save Database : {}", database);
        if (database.getId() != null) {
            throw new BadRequestAlertException("A new database cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Database result = databaseService.save(database);
        return ResponseEntity.created(new URI("/api/databases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /databases : Updates an existing database.
     *
     * @param database the database to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated database,
     * or with status 400 (Bad Request) if the database is not valid,
     * or with status 500 (Internal Server Error) if the database couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/databases")
    @Timed
    public ResponseEntity<Database> updateDatabase(@Valid @RequestBody Database database) throws URISyntaxException {
        log.debug("REST request to update Database : {}", database);
        if (database.getId() == null) {
            return createDatabase(database);
        }
        Database result = databaseService.save(database);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, database.getId().toString()))
            .body(result);
    }

    /**
     * GET  /databases : get all the databases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of databases in body
     */
    @GetMapping("/databases")
    @Timed
    public ResponseEntity<List<Database>> getAllDatabases(Pageable pageable) {
        log.debug("REST request to get a page of Databases");
        Page<Database> page = databaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/databases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /databases/:id : get the "id" database.
     *
     * @param id the id of the database to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the database, or with status 404 (Not Found)
     */
    @GetMapping("/databases/{id}")
    @Timed
    public ResponseEntity<Database> getDatabase(@PathVariable String id) {
        log.debug("REST request to get Database : {}", id);
        Database database = databaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(database));
    }

    @GetMapping("/databases/{id}/tables")
    @Timed
    public ResponseEntity<Database> getDatabaseTables(@PathVariable String id) {
        log.debug("REST request to get Database : {}", id);
        Database database = databaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(database));
    }


    /**
     * DELETE  /databases/:id : delete the "id" database.
     *
     * @param id the id of the database to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/databases/{id}")
    @Timed
    public ResponseEntity<Void> deleteDatabase(@PathVariable String id) {
        log.debug("REST request to delete Database : {}", id);
        databaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/databases?query=:query : search for the database corresponding
     * to the query.
     *
     * @param query the query of the database search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/databases")
    @Timed
    public ResponseEntity<List<Database>> searchDatabases(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Databases for query {}", query);
        Page<Database> page = databaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/databases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
