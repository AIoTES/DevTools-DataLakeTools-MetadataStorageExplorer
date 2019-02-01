package eu.activageproject.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.activageproject.tool.domain.Schema;
import eu.activageproject.tool.service.SchemaService;
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
 * REST controller for managing Schema.
 */
@RestController
@RequestMapping("/api")
public class SchemaResource {

    private final Logger log = LoggerFactory.getLogger(SchemaResource.class);

    private static final String ENTITY_NAME = "schema";

    private final SchemaService schemaService;

    public SchemaResource(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    /**
     * POST  /schemata : Create a new schema.
     *
     * @param schema the schema to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schema, or with status 400 (Bad Request) if the schema has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schemata")
    @Timed
    public ResponseEntity<Schema> createSchema(@Valid @RequestBody Schema schema) throws URISyntaxException {
        log.debug("REST request to save Schema : {}", schema);
        if (schema.getId() != null) {
            throw new BadRequestAlertException("A new schema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Schema result = schemaService.save(schema);
        return ResponseEntity.created(new URI("/api/schemata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schemata : Updates an existing schema.
     *
     * @param schema the schema to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schema,
     * or with status 400 (Bad Request) if the schema is not valid,
     * or with status 500 (Internal Server Error) if the schema couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schemata")
    @Timed
    public ResponseEntity<Schema> updateSchema(@Valid @RequestBody Schema schema) throws URISyntaxException {
        log.debug("REST request to update Schema : {}", schema);
        if (schema.getId() == null) {
            return createSchema(schema);
        }
        Schema result = schemaService.save(schema);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schema.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schemata : get all the schemata.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schemata in body
     */
    @GetMapping("/schemata")
    @Timed
    public ResponseEntity<List<Schema>> getAllSchemata(Pageable pageable) {
        log.debug("REST request to get a page of Schemata");
        Page<Schema> page = schemaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schemata");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schemata/:id : get the "id" schema.
     *
     * @param id the id of the schema to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schema, or with status 404 (Not Found)
     */
    @GetMapping("/schemata/{id}")
    @Timed
    public ResponseEntity<Schema> getSchema(@PathVariable String id) {
        log.debug("REST request to get Schema : {}", id);
        Schema schema = schemaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schema));
    }

    /**
     * DELETE  /schemata/:id : delete the "id" schema.
     *
     * @param id the id of the schema to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schemata/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchema(@PathVariable String id) {
        log.debug("REST request to delete Schema : {}", id);
        schemaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/schemata?query=:query : search for the schema corresponding
     * to the query.
     *
     * @param query the query of the schema search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/schemata")
    @Timed
    public ResponseEntity<List<Schema>> searchSchemata(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Schemata for query {}", query);
        Page<Schema> page = schemaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/schemata");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
