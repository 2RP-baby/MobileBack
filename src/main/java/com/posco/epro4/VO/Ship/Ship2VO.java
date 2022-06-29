package com.posco.epro4.VO.Ship;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ship2", schema = "public")
public class Ship2VO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ship2_id;
	private Integer seq;
	private Integer quantity_shipped;
	private Integer po_line_id;
	private Integer po_line_location_id;
	private Integer po_distribution_id;
	private Integer scc1_id;
	private Integer scc2_id;

}
