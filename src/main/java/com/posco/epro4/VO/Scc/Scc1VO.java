package com.posco.epro4.VO.Scc;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "scc1", schema = "public")
public class Scc1VO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scc1_id;
	private String shipment_num;
	private Integer employee_number;
	private String deliver_to_location;
	private String comment;
    @Temporal(TemporalType.TIMESTAMP)
	private Date send_date;
	private Integer po_header_id;
	private Integer po_release_id;
	private String subinventory;
}
