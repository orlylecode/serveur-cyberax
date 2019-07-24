package com.cogitech.cyberax.repository;

import com.cogitech.cyberax.domain.Terminal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Terminal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

}
