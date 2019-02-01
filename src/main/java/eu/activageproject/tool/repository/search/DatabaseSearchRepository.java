package eu.activageproject.tool.repository.search;

import eu.activageproject.tool.domain.Database;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Database entity.
 */
public interface DatabaseSearchRepository extends ElasticsearchRepository<Database, String> {
}
