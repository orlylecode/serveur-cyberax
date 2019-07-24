package com.cogitech.cyberax.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Task entity.
 * @author The JHipster team.
 */
@ApiModel(description = "Task entity. @author The JHipster team.")
@Entity
@Table(name = "list_attente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "listattente")
public class ListAttente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "version")
    private Integer version;

    @Column(name = "date_creation")
    private Integer dateCreation;

    @OneToMany(mappedBy = "listAttente")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Mise> mises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public ListAttente version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getDateCreation() {
        return dateCreation;
    }

    public ListAttente dateCreation(Integer dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Integer dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Set<Mise> getMises() {
        return mises;
    }

    public ListAttente mises(Set<Mise> mises) {
        this.mises = mises;
        return this;
    }

    public ListAttente addMises(Mise mise) {
        this.mises.add(mise);
        mise.setListAttente(this);
        return this;
    }

    public ListAttente removeMises(Mise mise) {
        this.mises.remove(mise);
        mise.setListAttente(null);
        return this;
    }

    public void setMises(Set<Mise> mises) {
        this.mises = mises;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListAttente)) {
            return false;
        }
        return id != null && id.equals(((ListAttente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ListAttente{" +
            "id=" + getId() +
            ", version=" + getVersion() +
            ", dateCreation=" + getDateCreation() +
            "}";
    }
}
