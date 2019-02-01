package eu.activageproject.tool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Table.
 */
@Document(collection = "table")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "table")
public class Table implements Serializable {

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

    @Field("data")
    private Object data;

    @Field("query")
    private String query;

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

    public Table db(String db) {
        this.db = db;
        return this;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTable() {
        return table;
    }

    public Table table(String table) {
        this.table = table;
        return this;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Object getData() {
        return data;
    }

    public Table data(Object data) {
        this.data = data;
        return this;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getQuery() {
        return query;
    }

    public Table query(String query) {
        this.query = query;
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
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
        Table table = (Table) o;
        if (table.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), table.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Table{" +
            "id=" + getId() +
            ", db='" + getDb() + "'" +
            ", table='" + getTable() + "'" +
            ", data='" + getData() + "'" +
            ", query='" + getQuery() + "'" +
            "}";
    }
}
