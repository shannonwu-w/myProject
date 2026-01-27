package com.myproject.server.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TABLE_LIST")
public class TableList {

    @Id
    @Column(name = "TABLE_ID")
    private Long tableId;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "TABLE_SIZE")
    private Integer tableSize;

}
