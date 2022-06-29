package com.posco.epro4.VO.Public;


import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "staff", schema = "public")
public class StaffVO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String dept_name;
    private String dept_code;

}