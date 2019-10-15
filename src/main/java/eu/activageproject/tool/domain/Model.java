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
    private String modelID;

    @NotNull
    @Size(min = 5, max = 20)
    @Field("name")
    private String name;

    @NotNull
    @Field("modelParams")
    private Object modelParams;

    @Field("created")
    private Instant created;

    @Field("updated")
    private Instant updated;

    @Field("created_by")
    private String createdBy;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
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

    public Object getModelParams() {
        return modelParams;
    }

    public Model modelParams(Object modelParams) {
        this.modelParams = modelParams;
        return this;
    }

    public void setModelParams(Object modelParams) {
        this.modelParams = modelParams;
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
        if (model.getModelID() == null || getModelID() == null) {
            return false;
        }
        return Objects.equals(getModelID(), model.getModelID());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getModelID());
    }

    @Override
    public String toString() {
        return "Model{" +
            "id=" + getModelID() +
            ", name='" + getName() + "'" +
            ", params='" + getModelParams() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
