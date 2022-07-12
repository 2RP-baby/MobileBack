package com.posco.epro4.DTO.Scc;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SccSearchInsertedOneDTO {
    
    private Integer scc1_id;
    private String  scc1_shipment_num;
    private String  scc1_deliver_to_location;
    private String  scc1_comment;
    private String  scc1_subinventory;

    private Integer scc2_id;
    private Integer scc2_quantity_ordered;
    private Date    scc2_need_by_date;
    private String  scc2_comment;

    private Integer po2_unit_price;

    private String  item;
    private String  item_uom;
    private String  item_description;

    private String  vendor_name;
}
