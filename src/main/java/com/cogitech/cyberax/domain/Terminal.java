package com.cogitech.cyberax.domain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * The Employee entity.
 */
@ApiModel(description = "The Employee entity.")
@Entity
@Table(name = "terminal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "terminal")
public class Terminal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    @Column(name = "message")
    private String message;

    @OneToOne
    @JoinColumn(unique = true)
    private Joueur joueur;

    @OneToOne
    @JoinColumn(unique = true)
    private Jeu jeuxEncour;

    @OneToOne
    @JoinColumn(unique = true)
    private Jeu jeuPrecedent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Terminal message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public Terminal joueur(Joueur joueur) {
        this.joueur = joueur;
        return this;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Jeu getJeuxEncour() {
        return jeuxEncour;
    }

    public Terminal jeuxEncour(Jeu jeu) {
        this.jeuxEncour = jeu;
        return this;
    }

    public void setJeuxEncour(Jeu jeu) {
        this.jeuxEncour = jeu;
    }

    public Jeu getJeuPrecedent() {
        return jeuPrecedent;
    }

    public Terminal jeuPrecedent(Jeu jeu) {
        this.jeuPrecedent = jeu;
        return this;
    }

    public void setJeuPrecedent(Jeu jeu) {
        this.jeuPrecedent = jeu;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Terminal)) {
            return false;
        }
        return id != null && id.equals(((Terminal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Terminal{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
