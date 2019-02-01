package eu.activageproject.tool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Database.
 */
@Document(collection = "database")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "database")
public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("db")
    private String db;

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

    public Database db(String db) {
        this.db = db;
        return this;
    }

    public void setDb(String db) {
        this.db = db;
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
        Database database = (Database) o;
        if (database.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), database.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Database{" +
            "id=" + getId() +
            ", db='" + getDb() + "'" +
            "}";
    }
}
