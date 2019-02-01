package eu.activageproject.tool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Query.
 */
@Document(collection = "query")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "query")
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 5, max = 20)
    @Field("db")
    private String db;

    @NotNull
    @Size(min = 5, max = 20)
    @Field("table")
    private String table;

    @NotNull
    @Field("query")
    private String query;

    @Field("output")
    private String output;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDb() {
        return db;
    }

    public Query db(String db) {
        this.db = db;
        return this;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTable() {
        return table;
    }

    public Query table(String table) {
        this.table = table;
        return this;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getQuery() {
        return query;
    }

    public Query query(String query) {
        this.query = query;
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOutput() {
        return output;
    }

    public Query output(String output) {
        this.output = output;
        return this;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    // simlife-needle-entity-add-getters-setters - Simlife will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Query query = (Query) o;
        if (query.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), query.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Query{" +
            "id=" + getId() +
            ", db='" + getDb() + "'" +
            ", table='" + getTable() + "'" +
            ", query='" + getQuery() + "'" +
            ", output='" + getOutput() + "'" +
            "}";
    }
}
