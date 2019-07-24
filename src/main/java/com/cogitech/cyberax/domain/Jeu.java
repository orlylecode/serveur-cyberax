package com.cogitech.cyberax.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "jeu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jeu")
public class Jeu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_lancement")
    private Instant dateLancement;

    @Column(name = "date_cloture")
    private Instant dateCloture;

    @Column(name = "encour")
    private Boolean encour;

    @Column(name = "pourcentage_mise")
    private Double pourcentageMise;

    @Column(name = "pourcentage_rebourt")
    private Double pourcentageRebourt;

    @OneToMany(mappedBy = "jeu")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Mise> mises = new HashSet<>();

    @OneToMany(mappedBy = "jeu")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Gagnant> gagnants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public Jeu dateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateLancement() {
        return dateLancement;
    }

    public Jeu dateLancement(Instant dateLancement) {
        this.dateLancement = dateLancement;
        return this;
    }

    public void setDateLancement(Instant dateLancement) {
        this.dateLancement = dateLancement;
    }

    public Instant getDateCloture() {
        return dateCloture;
    }

    public Jeu dateCloture(Instant dateCloture) {
        this.dateCloture = dateCloture;
        return this;
    }

    public void setDateCloture(Instant dateCloture) {
        this.dateCloture = dateCloture;
    }

    public Boolean isEncour() {
        return encour;
    }

    public Jeu encour(Boolean encour) {
        this.encour = encour;
        return this;
    }

    public void setEncour(Boolean encour) {
        this.encour = encour;
    }

    public Double getPourcentageMise() {
        return pourcentageMise;
    }

    public Jeu pourcentageMise(Double pourcentageMise) {
        this.pourcentageMise = pourcentageMise;
        return this;
    }

    public void setPourcentageMise(Double pourcentageMise) {
        this.pourcentageMise = pourcentageMise;
    }

    public Double getPourcentageRebourt() {
        return pourcentageRebourt;
    }

    public Jeu pourcentageRebourt(Double pourcentageRebourt) {
        this.pourcentageRebourt = pourcentageRebourt;
        return this;
    }

    public void setPourcentageRebourt(Double pourcentageRebourt) {
        this.pourcentageRebourt = pourcentageRebourt;
    }

    public Set<Mise> getMises() {
        return mises;
    }

    public Jeu mises(Set<Mise> mises) {
        this.mises = mises;
        return this;
    }

    public Jeu addMises(Mise mise) {
        this.mises.add(mise);
        mise.setJeu(this);
        return this;
    }

    public Jeu removeMises(Mise mise) {
        this.mises.remove(mise);
        mise.setJeu(null);
        return this;
    }

    public void setMises(Set<Mise> mises) {
        this.mises = mises;
    }

    public Set<Gagnant> getGagnants() {
        return gagnants;
    }

    public Jeu gagnants(Set<Gagnant> gagnants) {
        this.gagnants = gagnants;
        return this;
    }

    public Jeu addGagnants(Gagnant gagnant) {
        this.gagnants.add(gagnant);
        gagnant.setJeu(this);
        return this;
    }

    public Jeu removeGagnants(Gagnant gagnant) {
        this.gagnants.remove(gagnant);
        gagnant.setJeu(null);
        return this;
    }

    public void setGagnants(Set<Gagnant> gagnants) {
        this.gagnants = gagnants;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jeu)) {
            return false;
        }
        return id != null && id.equals(((Jeu) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Jeu{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateLancement='" + getDateLancement() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            ", encour='" + isEncour() + "'" +
            ", pourcentageMise=" + getPourcentageMise() +
            ", pourcentageRebourt=" + getPourcentageRebourt() +
            "}";
    }
}
