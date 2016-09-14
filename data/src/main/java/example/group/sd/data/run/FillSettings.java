package example.group.sd.data.run;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;


import example.group.sd.data.entity.Device;
import example.group.sd.data.repository.DeviceRepository;
import example.group.sd.data.utils.ListIp_settings;
import org.apache.commons.io.IOUtils;
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
	private static Gson gson = new GsonBuilder().create();

	String patchResources = System.getProperty("user.dir")
			+ File.separator
			+ "data"
			+ File.separator
			+ "src"
			+ File.separator
			+ "main"
			+ File.separator
			+ "resources";

	String filenameScannerProperties = patchResources
			+ File.separator
			+ "scanner.properties.json";

	String filenameSnmpProperties = patchResources
			+ File.separator
			+ "snmp.properties.json";


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

			Scanner_settings scanner_settings = gson.fromJson(readFileProperties(filenameScannerProperties), Scanner_settings.class);
			Snmp_settings snmp_settings = gson.fromJson(readFileProperties(filenameSnmpProperties), Snmp_settings.class);
			ListIp_settings listIpSettings = gson.fromJson(readFileProperties(filenameScannerProperties), ListIp_settings.class);

			Settings settings = new Settings();
			settings.setAlias("DEMO_SETTINGS");
			settings.setScanner_settings(gson.toJson(scanner_settings).toString());
			settings.setSnmp_settings(gson.toJson(snmp_settings).toString());

			Settings settingsIn = settingsRepo.findByAlias("DEMO_SETTINGS");
			if(settingsIn != null){
				settingsRepo.delete(settingsIn);
			}
			settingsRepo.save(settings);
			
			if(listIpSettings != null) {
				for (String strIp : listIpSettings.getListIp()) {
					ListIp listIp = new ListIp();
					listIp.setIp(strIp);
					listIp.setSettings(settings);
					listIpRepo.save(listIp);
				}
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

	String readFileProperties(String fileName){

		try {
			File file = new File(fileName);
			if(file.exists() ){

				List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
				String strFromFile = "";
				for(String line: lines){
					strFromFile = strFromFile.concat(line);
				}
				return strFromFile;
			} else {
				throw new java.lang.Exception("File " + fileName + " is not exists.");
			}

		} catch (Exception e) {

			LOG.error("Error in function Fill(), function readFileProperties");
			LOG.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}
}
