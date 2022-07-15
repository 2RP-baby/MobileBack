package com.posco.epro4.DTO.Ship;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShipSearchOneDTO {
    // scc1
    private Integer scc1_id;
    private String  shipment_num;
    private String  deliver_to_location;
    // scc2
    private Integer scc2_id;
    private Integer seq;
    private Integer quantity_ordered;
    private Date    need_by_date;
    private String  comment;
    private Integer po_distribution_id;
    // po1
    private Integer po_header_id;
    private String  po_num;
    // po2
    private Integer po_line_id;
    private Integer unit_price;
    private Integer quantity;
    // po4
    // private Integer po_line_location_id;
    // po5
    // private Integer po_distribution_id;
    // item
    private String  item;
    private String  uom;
    private String  description;
    // staff
    private String  staff_name;
    private Integer remaining;
}
