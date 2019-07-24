package com.cogitech.cyberax.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Gagnant.
 */
@Entity
@Table(name = "gagnant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gagnant")
public class Gagnant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "position")
    private Integer position;

    @Column(name = "date_gain")
    private Instant dateGain;

    @Column(name = "date_payment")
    private Instant datePayment;

    @ManyToOne
    @JsonIgnoreProperties("gagnants")
    private Jeu jeu;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Gagnant nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Gagnant prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public Gagnant telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getPosition() {
        return position;
    }

    public Gagnant position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Instant getDateGain() {
        return dateGain;
    }

    public Gagnant dateGain(Instant dateGain) {
        this.dateGain = dateGain;
        return this;
    }

    public void setDateGain(Instant dateGain) {
        this.dateGain = dateGain;
    }

    public Instant getDatePayment() {
        return datePayment;
    }

    public Gagnant datePayment(Instant datePayment) {
        this.datePayment = datePayment;
        return this;
    }

    public void setDatePayment(Instant datePayment) {
        this.datePayment = datePayment;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Gagnant jeu(Jeu jeu) {
        this.jeu = jeu;
        return this;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gagnant)) {
            return false;
        }
        return id != null && id.equals(((Gagnant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Gagnant{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", position=" + getPosition() +
            ", dateGain='" + getDateGain() + "'" +
            ", datePayment='" + getDatePayment() + "'" +
            "}";
    }
}
