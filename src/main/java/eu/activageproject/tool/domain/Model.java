package eu.activageproject.tool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Model.
 */
@Document(collection = "model")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "model")
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 5, max = 20)
    @Field("name")
    private String name;

    @NotNull
    @Field("params")
    private Object params;

    @Field("created")
    private Instant created;

    @Field("updated")
    private Instant updated;

    @Field("created_by")
    private String createdBy;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Model name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getParams() {
        return params;
    }

    public Model params(Object params) {
        this.params = params;
        return this;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public Instant getCreated() {
        return created;
    }

    public Model created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public Model updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Model createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        Model model = (Model) o;
        if (model.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), model.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Model{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", params='" + getParams() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
