package eu.activageproject.tool.repository.search;

import eu.activageproject.tool.domain.Table;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Table entity.
 */
public interface TableSearchRepository extends ElasticsearchRepository<Table, String> {
}
