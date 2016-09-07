package example.group.sd.rest.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.group.sd.data.entity.Device;

import example.group.sd.data.repository.DeviceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


//import com.stinscoman.centermind.data.repository.topology.ItemRepository;

import java.util.*;


/**
 * Created by Berez on 25.11.2015.
 */


@RestController
@RequestMapping(value = "api/scanner/")
public class ScannerController {

    @Autowired
    private DeviceRepository deviceRepo;


    static final Logger LOG = LoggerFactory.getLogger(ScannerController.class);
    private static Gson gson = new GsonBuilder().create();



    @RequestMapping(value = "/getAllDevices/", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getAllObjects() {

        List<Device> listDev = deviceRepo.getAllDevices();
        String res = null;

        if((listDev!=null)&&(listDev.size()>0)) {
            res = gson.toJson(listDev);
        }

        return res;
    }




}
