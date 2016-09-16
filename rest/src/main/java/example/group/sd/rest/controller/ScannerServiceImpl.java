package example.group.sd.rest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.group.sd.data.entity.Device;

import example.group.sd.data.repository.DeviceRepository;

import example.group.sd.rest.ResponseWrapperBuilder;
import example.group.sd.rest.RestConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


//import com.stinscoman.centermind.data.repository.topology.ItemRepository;

import javax.ws.rs.*;
import java.util.*;

@Component
@RequestMapping(value = "api/scanner/")
@RestController
public class ScannerServiceImpl implements ScannerService {

    @Autowired
    private DeviceRepository deviceRepo;

    static final Logger LOG = LoggerFactory.getLogger(ScannerServiceImpl.class);
    private static Gson gson = new GsonBuilder().create();

    //jersey
    @GET
    @Produces(RestConst.JSON_UTF8)
    @Path("/getAllDevices/")
    //Spring
    @RequestMapping(value = "/getAllDevices/", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    //@ResponseStatus(HttpStatus.OK)
    //@ResponseBody
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

    //jersey
    @GET
    @Produces(RestConst.JSON_UTF8)
    @Path("/addDevice/")
    @SuppressWarnings({"unchecked", "rawtypes"})
    //Spring
    @RequestMapping(value = "/addDevice/", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    public String addDevice(@QueryParam("ip") String ip, @QueryParam("description") String description) {

        try {
            Device newDevice = new Device();
            newDevice.setIp(ip);
            newDevice.setDescription(description);

            newDevice = deviceRepo.save(newDevice);

            if(newDevice.getId() != null) {
                return successfulCompletion(newDevice, "OK", "addDevice is completed successfully");
            } else {
                throw new java.lang.Exception("addDevice is failed");
            }

        } catch (Exception e) {
            return unsuccessfulEnd(e);
        }
    }

    @GET
    @Produces(RestConst.JSON_UTF8)
    @Path("/getDevice/")
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/getDevice/", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    public String getDevice(@QueryParam("ip") String ip) {

        try {

            List <Device> listDevices = deviceRepo.findByIp(ip);
            return successfulCompletion(listDevices, "OK", "getDevice is completed successfully");

        } catch (Exception e) {
            return unsuccessfulEnd(e);
        }
    }

    @DELETE
    @Produces(RestConst.JSON_UTF8)
    @Path("/deleteDevice/")
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/deleteDevice/", method = RequestMethod.DELETE, produces = "text/json;charset=UTF-8")
    public String deleteDevice(@QueryParam("id") Long id) {
        try {

            Device delDevices = deviceRepo.findOne(id);
            if(delDevices!=null){
                deviceRepo.delete(delDevices);
            }

            return successfulCompletion(null, "OK", "deleteDevice is completed successfully");

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
