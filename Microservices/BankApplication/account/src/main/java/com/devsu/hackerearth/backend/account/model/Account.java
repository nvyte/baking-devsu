package com.devsu.hackerearth.backend.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "accounts")
public class Account extends Base {

    private String number;
	private String type;
    @Column(name = "initial_amount")
	private double initialAmount;
    @Column(name = "is_active")
	private boolean isActive;

    @Column(name = "client_id")
    private Long clientId;
}
