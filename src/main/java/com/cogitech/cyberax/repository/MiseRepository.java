package com.cogitech.cyberax.repository;

import com.cogitech.cyberax.domain.Mise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Mise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MiseRepository extends JpaRepository<Mise, Long> {

}
