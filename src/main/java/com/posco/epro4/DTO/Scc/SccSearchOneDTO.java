package com.posco.epro4.DTO.Scc;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SccSearchOneDTO {
    private Integer po_header_id;
    private String  po_num;
    private String  comments;

    private Integer po_line_id;
    private Integer unit_price;
    private Integer quantity;
    private Integer quantity_ordered;

    private Integer po_distribution_id;
    private String  subinventory;

    private String  item_name;
    private String  item_uom;
    private String  item_description;
    
    private Integer vendor_id;
    private String  vendor_name;
    private Long    remaining;
}
