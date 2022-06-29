package com.posco.epro4.DTO.Ship;

import java.util.HashMap;
import java.util.List;

import lombok.*;

@Getter
@ToString
public class ShipInsertOneReqDTO {
    private HashMap<String, String> ship1;
    private List<HashMap<String, String>> ship2List;
}
