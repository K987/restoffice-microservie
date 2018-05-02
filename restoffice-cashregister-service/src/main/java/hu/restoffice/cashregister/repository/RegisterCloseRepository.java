package hu.restoffice.cashregister.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.restoffice.cashregister.entity.RegisterClose;

/**
 *
 */
public interface RegisterCloseRepository extends JpaRepository<RegisterClose, Long> {

    List<RegisterClose> findByCloseDateBetween(Date from, Date to);

    List<RegisterClose> findByCloseDate(Date day);

    @Query("select r from RegisterClose r where (r.id, r.closeNo) in (select r1.id, max(r1.closeNo) from RegisterClose r1 group by r1.id )")
    List<RegisterClose> findLastCloses();
}
