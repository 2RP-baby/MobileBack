package com.posco.epro4.VO.Po;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "po4", schema = "public")
public class Po4VO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer po_line_location_id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date promised_date;


    // private Integer po_shipment_num;
    // private Integer quantity;
    // private String amount ;
    // @Temporal(TemporalType.TIMESTAMP)
    // private Date need_by_date;
    
    // private String ship_to_organization_id;
    // private String tax_code;
    // private String inspection_required_flag;
    // private Integer qty_rcv_tolerance;
    // private String qty_rcv_exception_code;
    // private Integer quantity_received;
    // private Integer quantity_accepted;
    // private Integer quantity_rejected;
    // private Integer quantity_billed;
    // private Integer quantity_cancelled;


    // FK
    private Integer po_header_id;
    private Integer po_line_id;
    // @OneToMany(mappedBy = "po_line_location_id")
    // private List<Po5VO> po5Vo = new ArrayList<Po5VO>();
}
