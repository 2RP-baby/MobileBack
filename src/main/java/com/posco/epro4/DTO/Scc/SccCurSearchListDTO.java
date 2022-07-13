package com.posco.epro4.DTO.Scc;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SccCurSearchListDTO {
    private String  scc1_shipment_num; 
    private Date    scc1_send_date;
    private String  po1_comments;
    private String  po5_subinventory;
    private String  staff_dept_code; 
    private String  staff_name;
    // private Integer item_id; 
    // private String  item;
    private String  vendor_name;
}
