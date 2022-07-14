package com.shintheo.dgae.domaine;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.shintheo.dgae.common.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AnnouncementDto {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", unique = true, nullable = false)
	private UUID id;
	@Temporal(TemporalType.DATE)
	private Date departuredate;
	@Temporal(TemporalType.DATE)
	private Date arrivaldate;
	private String departuretown;
	private String destinationtown;
	private long quantity;
	private boolean computer;
	private String restriction;
	private boolean document;
	private Status status;
	private String cni;
	private String ticket;
	private String covidtest;
	private long price;
	private boolean validation;
	@ManyToOne
	private UserDto userDto;

}