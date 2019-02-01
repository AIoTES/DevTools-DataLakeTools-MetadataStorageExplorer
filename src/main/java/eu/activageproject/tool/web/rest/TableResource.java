package eu.activageproject.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.activageproject.tool.domain.Table;
import eu.activageproject.tool.service.TableService;
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
 * REST controller for managing Table.
 */
@RestController
@RequestMapping("/api")
public class TableResource {

    private final Logger log = LoggerFactory.getLogger(TableResource.class);

    private static final String ENTITY_NAME = "table";

    private final TableService tableService;

    public TableResource(TableService tableService) {
        this.tableService = tableService;
    }

    /**
     * POST  /tables : Create a new table.
     *
     * @param table the table to create
     * @return the ResponseEntity with status 201 (Created) and with body the new table, or with status 400 (Bad Request) if the table has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tables")
    @Timed
    public ResponseEntity<Table> createTable(@Valid @RequestBody Table table) throws URISyntaxException {
        log.debug("REST request to save Table : {}", table);
        if (table.getId() != null) {
            throw new BadRequestAlertException("A new table cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Table result = tableService.save(table);
        return ResponseEntity.created(new URI("/api/tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tables : Updates an existing table.
     *
     * @param table the table to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated table,
     * or with status 400 (Bad Request) if the table is not valid,
     * or with status 500 (Internal Server Error) if the table couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tables")
    @Timed
    public ResponseEntity<Table> updateTable(@Valid @RequestBody Table table) throws URISyntaxException {
        log.debug("REST request to update Table : {}", table);
        if (table.getId() == null) {
            return createTable(table);
        }
        Table result = tableService.save(table);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, table.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tables : get all the tables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tables in body
     */
    @GetMapping("/tables")
    @Timed
    public ResponseEntity<List<Table>> getAllTables(Pageable pageable) {
        log.debug("REST request to get a page of Tables");
        Page<Table> page = tableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tables/:id : get the "id" table.
     *
     * @param id the id of the table to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the table, or with status 404 (Not Found)
     */
    @GetMapping("/tables/{id}")
    @Timed
    public ResponseEntity<Table> getTable(@PathVariable String id) {
        log.debug("REST request to get Table : {}", id);
        Table table = tableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(table));
    }

    /**
     * DELETE  /tables/:id : delete the "id" table.
     *
     * @param id the id of the table to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteTable(@PathVariable String id) {
        log.debug("REST request to delete Table : {}", id);
        tableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/tables?query=:query : search for the table corresponding
     * to the query.
     *
     * @param query the query of the table search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tables")
    @Timed
    public ResponseEntity<List<Table>> searchTables(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tables for query {}", query);
        Page<Table> page = tableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
