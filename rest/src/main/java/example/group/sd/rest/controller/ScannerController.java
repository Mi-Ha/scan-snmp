package example.group.sd.rest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.group.sd.data.entity.Device;

import example.group.sd.data.repository.DeviceRepository;

import example.group.sd.rest.ResponseWrapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


//import com.stinscoman.centermind.data.repository.topology.ItemRepository;

import java.util.*;

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
    public String getAllDevices() {

        try {
            List<Device> listDev = deviceRepo.getAllDevices();
            String res = null;

            if((listDev!=null)&&(listDev.size()>0)) {

                return successfulCompletion(listDev, "OK", "getAllDevices is completed successfully");
            }

            return successfulCompletion(null, "OK", "getAllDevices is completed successfully");

        } catch (Exception e) {
            return unsuccessfulEnd(e);
        }
    }

    String successfulCompletion(Object obj, String strCode, String strMessage) {

        try {
            String strJSON = "";
            if (obj != null)
                strJSON = gson.toJson(obj);
            else
                strJSON = "";
            return gson.toJson(new ResponseWrapperBuilder().setData(strJSON).info(strCode, strMessage).build());

        } catch (Exception e) {
            return unsuccessfulEnd(e);
        }

    }


    String unsuccessfulEnd(Exception e) {
        LOG.error(e.getMessage());
        return gson.toJson(new ResponseWrapperBuilder().setException(new Exception(e)).build());
    }

}
