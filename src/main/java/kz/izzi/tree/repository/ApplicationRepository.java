package kz.izzi.tree.repository;

import kz.izzi.tree.domain.Application;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    @Query("select application from Application application where application.user.login = ?#{principal.username}")
    List<Application> findByUserIsCurrentUser();
    
}
