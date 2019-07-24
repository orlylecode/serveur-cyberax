package com.cogitech.cyberax.repository;

import com.cogitech.cyberax.domain.ListAttente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ListAttente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ListAttenteRepository extends JpaRepository<ListAttente, Long> {

}
