package com.posco.epro4.VO.Ship;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ship1", schema = "public")
public class Ship1VO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ship1_id;
	private String shipment_num;
    @Temporal(TemporalType.TIMESTAMP)
	private Date expected_receipt_date;
	private String contact_name;
	private String note_to_receiver;
    @Temporal(TemporalType.TIMESTAMP)
	private Date shipped_date;
    @Temporal(TemporalType.TIMESTAMP)
	private Date send_date;
	private Integer po_header_id;
	private Integer po_release_id;
	private Integer scc1_id;

}
