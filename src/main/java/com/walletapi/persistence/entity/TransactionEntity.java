package com.walletapi.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@GenericGenerator(name = "transactionId", strategy = "uuid2")
@Table(name = "TRANSACTION")
public class TransactionEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "transactionId")
    private String id;

    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Column(name = "PLAYER_ID")
    private Long playerId;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DIRECTION")
    private Integer direction;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}