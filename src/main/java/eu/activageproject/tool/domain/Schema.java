package eu.activageproject.tool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Schema.
 */
@Document(collection = "schema")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schema")
public class Schema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 5, max = 20)
    @Field("entity")
    private String entity;

    @Field("properties")
    private Object properties;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public Schema entity(String entity) {
        this.entity = entity;
        return this;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Object getProperties() {
        return properties;
    }

    public Schema properties(Object properties) {
        this.properties = properties;
        return this;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
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
        Schema schema = (Schema) o;
        if (schema.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schema.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Schema{" +
            "id=" + getId() +
            ", entity='" + getEntity() + "'" +
            ", properties='" + getProperties() + "'" +
            "}";
    }
}
