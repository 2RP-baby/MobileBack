package com.posco.epro4.VO.Public;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "item", schema = "public")
public class ItemVO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer item_id;
    private String item;
    private String description;
    private String uom;
    // private Integer category_id;
    // private String category;
    // private Integer organization_id;
    // private String group_name;

}