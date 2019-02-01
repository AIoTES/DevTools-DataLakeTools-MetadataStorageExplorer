package eu.activageproject.tool.repository.search;

import eu.activageproject.tool.domain.Model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Model entity.
 */
public interface ModelSearchRepository extends ElasticsearchRepository<Model, String> {
}
