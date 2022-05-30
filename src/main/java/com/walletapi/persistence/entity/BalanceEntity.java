package com.walletapi.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = "ID", strategy = "uuid2")
@Table(name = "BALANCE")
public class BalanceEntity {

    @Id
    @GeneratedValue(generator = "balanceIdSeq")
    @SequenceGenerator(
            name = "balanceIdSeq",
            sequenceName = "balanceIdSeq",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "PLAYER_ID")
    private Long playerId;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "DESCRIPTION")
    private String description;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}