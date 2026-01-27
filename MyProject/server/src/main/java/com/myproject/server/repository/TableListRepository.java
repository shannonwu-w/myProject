package com.myproject.server.repository;

import com.myproject.server.domain.entity.TableList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableListRepository extends JpaRepository<TableList,Long> {
}
