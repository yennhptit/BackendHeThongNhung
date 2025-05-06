package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ViolationType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Violation.
 */
@Entity
@Table(name = "violation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Violation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private Float value;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ViolationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trips", "violations" }, allowSetters = true)
    private Driver driver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Violation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValue() {
        return this.value;
    }

    public Violation value(Float value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Violation timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public ViolationType getType() {
        return this.type;
    }

    public Violation type(ViolationType type) {
        this.setType(type);
        return this;
    }

    public void setType(ViolationType type) {
        this.type = type;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Violation driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Violation)) {
            return false;
        }
        return getId() != null && getId().equals(((Violation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Violation{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", timestamp='" + getTimestamp() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
