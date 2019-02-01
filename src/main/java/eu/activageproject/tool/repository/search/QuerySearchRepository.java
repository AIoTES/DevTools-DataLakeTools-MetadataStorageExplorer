package eu.activageproject.tool.repository.search;

import eu.activageproject.tool.domain.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Query entity.
 */
public interface QuerySearchRepository extends ElasticsearchRepository<Query, String> {
}
