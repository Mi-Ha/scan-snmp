package example.group.sd.scanner;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.group.sd.data.entity.Device;
import example.group.sd.data.entity.ListIp;
import example.group.sd.data.entity.Settings;
import example.group.sd.data.repository.DeviceRepository;
import example.group.sd.data.repository.ListIpRepository;
import example.group.sd.data.repository.SettingsRepository;
import example.group.sd.data.utils.Scanner_settings;
import example.group.sd.data.utils.Snmp_settings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;

/**
 * Created by APetko on 05.10.2015.
 */
@Component
public class ScannerBean {

    Logger LOGGER = Logger.getLogger(ScannerBean.class);
    Thread_Scan objThread_Scan;

    @Autowired
    private DeviceRepository deviceRepo;

    @Autowired
    private SettingsRepository SettingsRepo;

    @Autowired
    private ListIpRepository listIpRepo;

    @PostConstruct
    public void run(){
        LOGGER.info(" ***** Scanner - running ***** ");

        Settings settings = SettingsRepo.findByAlias("DEMO_SETTINGS");
        objThread_Scan = new Thread_Scan(settings, this);
        objThread_Scan.execute();

        LOGGER.info(" ***** Scanner - run ***** ");
    }

    @PreDestroy
    public void stop() {

        LOGGER.info(" ***** Scanner - stop ***** ");
        objThread_Scan.stopThreadNetScan();
    }

    public void saveDeviceInfo(String ip, String description){

        Device deviceTmp;

        try {
            List<Device> listDevice = deviceRepo.findByIp(ip);
            if (listDevice.size() > 0) {
                deviceTmp = listDevice.get(0);
            } else {
                deviceTmp = new Device();
                deviceTmp.setIp(ip);
            }
            deviceTmp.setDescription(description);
            deviceRepo.save(deviceTmp);

            if (deviceTmp.getId() == null) {
                throw new java.lang.Exception("Device ID is null");
            }
        }catch (Exception e){

            LOGGER.error("Error save device to Data Base: " + e.getMessage());
        }

    }

 }
