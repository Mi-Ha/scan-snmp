package example.group.sd.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import example.group.sd.rest.RestConst;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.ws.rs.*;
import java.text.ParseException;
/**
 * Created by MNikitin on 15.09.2016.
 */
@Path("api/scanner/")
@Produces(RestConst.JSON_UTF8)
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public interface ScannerService {

    @GET
    @Produces(RestConst.JSON_UTF8)
    @Path("/getAllDevices/")
    public String getAllDevices();

    @GET
    @Produces(RestConst.JSON_UTF8)
    @Path("/addDevice/")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addDevice(@QueryParam("ip") String ip, @QueryParam("description") String description);

    @GET
    @Produces(RestConst.JSON_UTF8)
    @Path("/getDevice/")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String getDevice(@QueryParam("ip") String ip);

    @DELETE
    @Produces(RestConst.JSON_UTF8)
    @Path("/deleteDevice/")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String deleteDevice(@QueryParam("id") Long id);


}
