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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", unique = true, nullable = false)
	private UUID id;
	private String content;
	private Status status;
	@ManyToOne
	private ReservationDto reservationDto;
	@ManyToOne
	private UserDto sendermessage;
	@ManyToOne
	private UserDto receivermessage;
	@Temporal(TemporalType.DATE)
	private Date date;
}
