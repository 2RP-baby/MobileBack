package com.posco.epro4.Contoller.Ship;

import java.util.HashMap;
import java.util.List;

import com.posco.epro4.Contoller.PublicMethod.*;
import com.posco.epro4.DTO.Ship.ShipInsertOneReqDTO;
import com.posco.epro4.Repository.Ship.ShipRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ship")
@CrossOrigin(origins = "*")
public class ShipContoller {

    @Autowired
    private ShipRepository shipRepository;

    
    @RequestMapping(value = "shipSearchList", method = RequestMethod.POST)
    public Object shipSearchList(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return shipRepository.shipSearchList(map);
    }

    @RequestMapping(value = "shipSearchOne", method = RequestMethod.POST)
    public Object shipSearchOne(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return shipRepository.shipSearchOne(map);
    }

    @RequestMapping(value = "shipInsertOne", method = RequestMethod.POST)
    public Object shipInsertOne(@RequestBody ShipInsertOneReqDTO dto) {

        //System.out.println("param : " + dto);

        HashMap<String, String> ship1 = dto.getShip1();
        List<HashMap<String, String>> ship2List = dto.getShip2List();

        PMethod.setEmptyValueInMap(ship1, null);
        for (HashMap<String,String> ship2 : ship2List) {
            PMethod.setEmptyValueInMap(ship2, null);
        }

        return shipRepository.shipInsertOne(ship1, ship2List);
    }
    
    @RequestMapping(value = "shipCurSearchList", method = RequestMethod.POST)
    public Object shipCurSearchList(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return shipRepository.shipCurSearchList(map);
    }

    @RequestMapping(value = "shipSearchInsertedOne", method = RequestMethod.POST)
    public Object shipSearchInsertedOne(@RequestBody HashMap<String, String> map) {

        //System.out.println("param : " + map);

        PMethod.setEmptyValueInMap(map, null);

        return shipRepository.shipSearchInsertedOne(map);
    }
    
}
