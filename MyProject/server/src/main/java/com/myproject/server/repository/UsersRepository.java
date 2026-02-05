package com.myproject.server.repository;


import com.myproject.server.domain.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
   Optional<Users> findByUserId(Long userId);
   Optional<Users> findByEmail(String Email);



    @Query(
            value = "SELECT * FROM USERS u WHERE " +
                    "LOWER(u.EMAIL) LIKE LOWER('%' || :keyword || '%') OR " +
                    "CAST(u.PHONE AS CHAR(20)) LIKE '%' || :keyword || '%' OR " +
                    "LOWER(u.USERNAME) LIKE LOWER('%' || :keyword || '%') OR " +
                    "LOWER(CAST(u.ROLE AS CHAR(20))) LIKE LOWER('%' || :keyword || '%') " +
                    "ORDER BY u.USER_ID DESC",
            countQuery = "SELECT count(*) FROM USERS u WHERE " +
                    "LOWER(u.EMAIL) LIKE LOWER('%' || :keyword || '%') OR " +
                    "CAST(u.PHONE AS CHAR(20)) LIKE '%' || :keyword || '%' OR " +
                    "LOWER(u.USERNAME) LIKE LOWER('%' || :keyword || '%') OR " +
                    "LOWER(CAST(u.ROLE AS CHAR(20))) LIKE LOWER('%' || :keyword || '%')",
            nativeQuery = true
    )
    Page<Users> findByAllFields(@Param("keyword") String keyword, Pageable pageable);

}
