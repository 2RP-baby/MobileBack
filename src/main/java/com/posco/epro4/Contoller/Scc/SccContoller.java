package com.posco.epro4.Contoller.Scc;

import java.util.HashMap;
import java.util.List;

import com.posco.epro4.Contoller.PublicMethod.*;
import com.posco.epro4.DTO.Scc.SccInsertOneReqDTO;
import com.posco.epro4.Repository.Scc.SccRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scc")
@CrossOrigin(origins = "*")
public class SccContoller {

    @Autowired
    private SccRepository sccRepository;

    
    @RequestMapping(value = "sccSearchList", method = RequestMethod.POST)
    public Object sccSearchList(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return sccRepository.sccSearchList(map);
    }

    @RequestMapping(value = "sccSearchOne", method = RequestMethod.POST)
    public Object sccSearchOne(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return sccRepository.sccSearchOne(map);
    }

    @RequestMapping(value = "sccInsertOne", method = RequestMethod.POST)
    public Object sccInsertOne(@RequestBody SccInsertOneReqDTO dto) {

        //System.out.println("param : " + dto);

        HashMap<String, String> scc1 = dto.getScc1();
        List<HashMap<String, String>> scc2List = dto.getScc2List();

        PMethod.setEmptyValueInMap(scc1, null);
        for (HashMap<String,String> scc2 : scc2List) {
            PMethod.setEmptyValueInMap(scc2, null);
        }

        return sccRepository.sccInsertOne(scc1, scc2List);
    }
    
    @RequestMapping(value = "sccCurSearchList", method = RequestMethod.POST)
    public Object sccCurSearchList(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return sccRepository.sccCurSearchList(map);
    }
    
    @RequestMapping(value = "sccSearchInsertedOne", method = RequestMethod.POST)
    public Object sccSearchInsertedOne(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return sccRepository.sccSearchInsertedOne(map);
    }
}
