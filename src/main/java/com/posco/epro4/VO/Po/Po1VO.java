package com.posco.epro4.VO.Po;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "po1", schema = "public")
public class Po1VO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer po_header_id;
    private String po_num; //
    private String comments; // 
    private Integer vendor_id; // join
    
    // @OneToMany(mappedBy = "po_header_id")
    // private List<Po2VO> po2VoList = new ArrayList<Po2VO>();

}