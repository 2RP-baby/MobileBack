package com.posco.epro4.DTO.Ship;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShipCurSearchListDTO {
    private String shipment_num; 
    private Date   send_date; 
    private String note_to_receiver; 
    private String contact_name;
    private String deliver_to_location;
    private String subinventory;
}
