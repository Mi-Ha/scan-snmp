package example.group.sd.rest.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.group.sd.data.entity.Device;
import example.group.sd.data.repository.DeviceRepository;
import example.group.sd.rest.ResponseWrapper;
import example.group.sd.rest.base.TestRestBase;
import example.group.sd.rest.config.TestConfiguration;
import example.group.sd.rest.controller.ScannerService;
import example.group.sd.rest.controller.ScannerServiceImpl;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import org.junit.Assert;

/**
 * Created by MNikitin on 15.09.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(OrderedTestRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
@Configurable
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScannerControllerTest extends TestRestBase {


    static final Logger LOG = LoggerFactory.getLogger(ScannerControllerTest.class);
    public static Class<?>[] RestClasses = {ScannerServiceImpl.class};
    private static Gson gson = new GsonBuilder().create();

    private ScannerService scannerService;

    @Autowired
    private DeviceRepository deviceRepo;

    @Before
    public void init() {
        scannerService = WebResourceFactory.newResource(ScannerService.class, target());
    }

    @Test
    public void test001() {
        try {
            //Создаем тестовый объект
            LOG.info("Test: 'addDevice' invoke");
            String strResponce = scannerService.addDevice("0.0.0.0", "Test Device");
            ResponseWrapper rw = (ResponseWrapper) gson.fromJson(strResponce, ResponseWrapper.class);
            if(!rw.getSuccess()){
                throw new java.lang.Exception("Test 'addDevice' is failed");
            }
            Device addDevice =  (Device) gson.fromJson(rw.getData().toString(), Device.class);

            //Извлекаем тестовый объект
            LOG.info("Test: 'getDevice' invoke");
            strResponce = scannerService.getDevice("0.0.0.0");
            rw = (ResponseWrapper) gson.fromJson(strResponce, ResponseWrapper.class);
            if(!rw.getSuccess()){
                throw new java.lang.Exception("Test 'getDevice' is failed");
            }
            List<Device> listDevices = gson.fromJson(rw.getData().toString(), List.class);
            if(listDevices.size()==0){
                throw new java.lang.Exception("Test 'getDevice' is failed");
            }

            //Извлекаем все объекты
            LOG.info("Test: 'getAllDevices' invoke");
            strResponce = scannerService.getAllDevices();
            rw = (ResponseWrapper) gson.fromJson(strResponce, ResponseWrapper.class);
            if(!rw.getSuccess()){
                throw new java.lang.Exception("Test 'getAllDevices' is failed");
            }
            listDevices.clear();
            listDevices = gson.fromJson(rw.getData().toString(), List.class);
            if(listDevices.size()<1){
                throw new java.lang.Exception("Test 'getAllDevices' is failed");
            }

            //Удаляем тестовый объект
            LOG.info("Test: 'deleteDevice' invoke");
            strResponce = scannerService.deleteDevice(addDevice.getId());
            rw = (ResponseWrapper) gson.fromJson(strResponce, ResponseWrapper.class);

            Assert.assertTrue(rw.getSuccess());

        } catch (Exception e) {
            LOG.error(e.getMessage());
            //logger.error(e.getMessage());

            Assert.fail();
        }
    }

    @Override
    protected Class<?>[] getServiceClasses() {

        return RestClasses;
    }

    @Override
    protected String getSpringConfigLocation() {

        return "classpath:dataSourceTestContext.xml";
    }
}
