package com.oyku.event_reservation_api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 100)
	private String location;

	@Column(nullable = false, length = 200)
	private String address;

	@Column(nullable = false)
	private LocalDateTime eventDate;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal ticketPrice;

	@Builder.Default
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Seat> seats = new ArrayList<>();
}
