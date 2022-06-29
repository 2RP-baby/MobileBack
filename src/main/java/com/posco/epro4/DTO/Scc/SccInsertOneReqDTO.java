package com.posco.epro4.DTO.Scc;

import java.util.HashMap;
import java.util.List;

import lombok.*;

@Getter
@ToString
public class SccInsertOneReqDTO {
    private HashMap<String, String> scc1;
    private List<HashMap<String, String>> scc2List;
}
