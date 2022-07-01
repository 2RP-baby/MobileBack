package com.posco.epro4.VO.Po;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "po2", schema = "public")
public class Po2VO {
    // Primary Key 설정
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer po_line_id;
    private Integer item_id; // join
    private Integer unit_price;
    // private Integer quantity;
    private Integer mat_bpa_agree_qt;
    //FK
    private Integer po_header_id;

    // private String closed_code;
    // private Integer po_line_num;
    // private Integer category_id;
    // private String  item_description;
    // private String unit_meas_lookup_code;
    // private Integer mat_bpa_agree_qt;
    // private Long contract_amount;
    // @OneToMany(mappedBy = "po_line_id")
    // private List<Po4VO> po4VoList = new ArrayList<Po4VO>();
}
