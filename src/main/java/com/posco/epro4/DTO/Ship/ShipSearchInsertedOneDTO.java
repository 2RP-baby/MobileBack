package com.posco.epro4.DTO.Ship;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShipSearchInsertedOneDTO {
    // ship1
    private String shipment_num; 
    private Date   shipped_date; 
    private Date   expected_receipt_date;
    private String contact_name; 
    private String note_to_receiver;
    // scc1
    private String deliver_to_location; 
    private String comment;
    // ship2
    private Integer quantity_shipped;
    // scc2
    private Integer quantity_ordered; 
    private Date    need_by_date;
    // po2
    private Integer unit_price;
    // staff
    private String staff_name;
    // item
    private String item; 
    private String item_uom; 
    private String item_description;
}
