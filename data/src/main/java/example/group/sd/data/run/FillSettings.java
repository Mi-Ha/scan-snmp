package example.group.sd.data.run;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;


import example.group.sd.data.entity.Device;
import example.group.sd.data.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import example.group.sd.data.config.ConfigurationBase;
import example.group.sd.data.entity.ListIp;
import example.group.sd.data.entity.Settings;
import example.group.sd.data.repository.ListIpRepository;
import example.group.sd.data.repository.SettingsRepository;
import example.group.sd.data.utils.Scanner_settings;
import example.group.sd.data.utils.Snmp_settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class FillSettings {

	static final Logger LOG = LoggerFactory.getLogger(FillSettings.class);
	//private static ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	private static Gson gson = new GsonBuilder().create();
	
	@Autowired
    private SettingsRepository settingsRepo;
	
	@Autowired
    private ListIpRepository listIpRepo;

	@Autowired
	private DeviceRepository deviceRepo;
	
	public static void main(String[] args) {
		LOG.info("invoke FillSettings.main");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ConfigurationBase.class);
        ctx.refresh();
        
        FillSettings bean = ctx.getBean(FillSettings.class);
        bean.Fill();
	}
	
	public boolean Fill() {
		try {
		
			Scanner_settings scanner_settings = new Scanner_settings();
			scanner_settings.setIp("172.20.6.254");
			scanner_settings.setGetNetworkPrefixLengthFromSettings(true);
			scanner_settings.setNetworkPrefixLength(29);
			scanner_settings.setNetScanPeriod(5000L);
			scanner_settings.setSendWorkingEvent(5000L);
			scanner_settings.setWaitingForPollingDevices(0L);
			
			Snmp_settings snmp_settings = new Snmp_settings();
			snmp_settings.setSmnpPort(161);
			snmp_settings.setSmnpTransportProtokol("udp");
			snmp_settings.setSmnpCOMMUNITY("public");
			snmp_settings.setSmnpRETRIES(3);
			snmp_settings.setSmnpTIMEOUT(1000);
			snmp_settings.setSmnpVersion("version2c");;
			
			Settings settings = new Settings();
			settings.setAlias("DEMO_SETTINGS");
			
			settings.setScanner_settings(gson.toJson(scanner_settings).toString());
			settings.setSnmp_settings(gson.toJson(snmp_settings).toString());

			Settings settingsIn = settingsRepo.findByAlias("DEMO_SETTINGS");
			if(settingsIn != null){
				settingsRepo.delete(settingsIn);
			}
			
			settingsRepo.save(settings);
			
			//String []arrStrIp = {};
			String []arrStrIp = {"127.0.0.1","172.20.6.254","172.20.6.255"};

			for(String strIp : arrStrIp){
				ListIp listIp = new ListIp();
				listIp.setIp(strIp);
				listIp.setSettings(settings);
				listIpRepo.save(listIp);
			}

			LOG.info("  ***  Settings is complected!  ***  ");

        } catch (Exception e) {

            LOG.error("Error in function Fill()");
            LOG.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
		return true;
	}

}
