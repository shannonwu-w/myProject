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



   @Query(value = "SELECT r FROM　USERS r WHERE "+
           "LOWER(r.email) LIKE LOWER(:keyword) OR "+
           "CAST(r.phone AS String) LIKE :keyword OR "+
           "LOWER(r.userName) LIKE :keyword OR "+
           "CAST(r.role) LIKE :keyword "+
           "ORDER BY　r.userId　DESC ", nativeQuery = true
   )
   Page<Users> findByAllFields(@Param("keyword") String keyword, Pageable pageable);


    @Override
    Page<Users> findAll(Pageable pageable);
}
