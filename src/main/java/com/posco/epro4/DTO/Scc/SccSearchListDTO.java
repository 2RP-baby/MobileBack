package com.posco.epro4.DTO.Scc;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SccSearchListDTO {
    private String po_num;
    private String comments;
    private Date promised_date;
    private String subinventory;
    private String staff_dept_code;
    private String staff_name;
    private String vendor_name;
}
