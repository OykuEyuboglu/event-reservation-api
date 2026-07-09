package com.oyku.event_reservation_api.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.oyku.event_reservation_api.enums.Role;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private String id = UUID.randomUUID().toString();

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(unique= true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();
    
    

}
