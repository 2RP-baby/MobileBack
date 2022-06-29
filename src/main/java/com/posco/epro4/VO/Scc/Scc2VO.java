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
@Table(name = "scc2", schema = "public")
public class Scc2VO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scc2_id;
    private Integer seq;
    private Integer quantity_ordered;
    private String  cost_center;
    private String  comment;
    @Temporal(TemporalType.TIMESTAMP)
	private Date    need_by_date;
    // FK
    private Integer scc1_id;
    private Integer po_line_id;
	private Integer po_distribution_id;
}
