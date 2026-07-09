package com.oyku.event_reservation_api.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
