package eu.activageproject.tool.repository.search;

import eu.activageproject.tool.domain.Schema;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Schema entity.
 */
public interface SchemaSearchRepository extends ElasticsearchRepository<Schema, String> {
}
