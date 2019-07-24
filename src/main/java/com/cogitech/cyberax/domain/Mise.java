package com.cogitech.cyberax.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Mise.
 */
@Entity
@Table(name = "mise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "mise")
public class Mise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "date_mise")
    private Instant dateMise;

    @Column(name = "date_validation")
    private Instant dateValidation;

    @Column(name = "etat")
    private Integer etat;

    @Column(name = "position_clic")
    private Integer positionClic;

    @ManyToOne
    @JsonIgnoreProperties("mises")
    private Joueur joueur;

    @ManyToOne
    @JsonIgnoreProperties("mises")
    private Jeu jeu;

    @ManyToOne
    @JsonIgnoreProperties("mises")
    private ListAttente listAttente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public Mise montant(Double montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Instant getDateMise() {
        return dateMise;
    }

    public Mise dateMise(Instant dateMise) {
        this.dateMise = dateMise;
        return this;
    }

    public void setDateMise(Instant dateMise) {
        this.dateMise = dateMise;
    }

    public Instant getDateValidation() {
        return dateValidation;
    }

    public Mise dateValidation(Instant dateValidation) {
        this.dateValidation = dateValidation;
        return this;
    }

    public void setDateValidation(Instant dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Integer getEtat() {
        return etat;
    }

    public Mise etat(Integer etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public Integer getPositionClic() {
        return positionClic;
    }

    public Mise positionClic(Integer positionClic) {
        this.positionClic = positionClic;
        return this;
    }

    public void setPositionClic(Integer positionClic) {
        this.positionClic = positionClic;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public Mise joueur(Joueur joueur) {
        this.joueur = joueur;
        return this;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Mise jeu(Jeu jeu) {
        this.jeu = jeu;
        return this;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public ListAttente getListAttente() {
        return listAttente;
    }

    public Mise listAttente(ListAttente listAttente) {
        this.listAttente = listAttente;
        return this;
    }

    public void setListAttente(ListAttente listAttente) {
        this.listAttente = listAttente;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mise)) {
            return false;
        }
        return id != null && id.equals(((Mise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Mise{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", dateMise='" + getDateMise() + "'" +
            ", dateValidation='" + getDateValidation() + "'" +
            ", etat=" + getEtat() +
            ", positionClic=" + getPositionClic() +
            "}";
    }
}
