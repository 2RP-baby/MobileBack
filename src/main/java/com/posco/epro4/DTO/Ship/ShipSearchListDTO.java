package com.posco.epro4.DTO.Ship;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShipSearchListDTO {
    private Integer scc1_id;
    private String  shipment_num;
    private String  po_num;
    private String  comments;
    private String  staff_name;
    private String  deliver_to_location;
    private Date    send_date;
    private Long    scc_amount;
}
