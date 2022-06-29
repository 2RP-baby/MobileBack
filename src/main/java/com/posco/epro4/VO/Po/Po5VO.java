package com.posco.epro4.VO.Po;


import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "po5", schema = "public")
public class Po5VO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer po_distribution_id;

    // private Integer req_distribution_id;
    private Integer request_person_id;
    // private String deliver_to_location_id;
    private String destination_subinventory;
    // private String account_nm;

    // FK
    private Integer po_header_id;
    private Integer po_line_id;
    private Integer po_line_location_id;
}
