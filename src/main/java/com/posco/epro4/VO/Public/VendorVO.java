package com.posco.epro4.VO.Public;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "vendor1", schema = "public")
public class VendorVO {
    // Primary Key 설정
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer vendor_id; 
    private Integer vendor_site_id;

    private String vendor_name;
    private String contact_name;
    private String contact_email_address;
    private String contact_mobile;
    private String vendor_location;
}
