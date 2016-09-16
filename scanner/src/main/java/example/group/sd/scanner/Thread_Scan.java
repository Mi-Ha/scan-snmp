package example.group.sd.scanner;

/*
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.group.sd.data.entity.ListIp;
import example.group.sd.data.entity.Settings;
import example.group.sd.data.utils.Scanner_settings;
import example.group.sd.data.utils.Snmp_settings;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Set;

/**
 * Created by MNikitin on 05.09.2016.
 */
public class Thread_Scan  implements ResponseListener,Runnable{

    private static final Logger LOGGER = Logger.getLogger(Thread_Scan.class);
    private static Gson gson = new GsonBuilder().create();
    Thread thrd;

    //DiscoverySettings
    String strURL = null;                                           //URL модуля
    String strMyIp;
    int discoveryNetworkPrefixLength = 32;                          //Ранг маски сети, заданный в настройках
    boolean discoveryGetNetworkPrefixLengthFromSettings = false;    //Если true, то диапазон IP-адресов вычислять на основе URL и маски из настроек. Иначе - автоматически.
    int waitingForPollingDevices = 200;                             //Время (мс), сколько ждем ответы от устройств после того, как всем устройствам послали запросы.
    int discoveryNetScanPeriod = 5000;                              //Период запуска циклограммы опроса сети


    //SNMP
    private Snmp snmp = null;                       //Объект SNMP
    private TransportMapping transport = null;      //Транспорт
    String smnpPort = "161";                        //Порт внешнего устройства для связи по SNMP
    String smnpTransportProtokol;                   //Транспортный протокол, по которому должен работать SNMP (настройка SNMP протокола: udp, tcp)
    String smnpCOMMUNITY = "public";                //Snmp-коммунити
    int smnpRETRIES   = 3;                          //Число попыток отправки запроса (настройка SNMP протокола)
    long smnpTIMEOUT   = 1000L;                     //Ожидание ответа от устройства (настройка SNMP протокола)
    String smnpVersion = "version2c";               //Версия протокола SNMP (настройка SNMP протокола: version1, version2c, version3)

    volatile boolean bGetDataOn = false;            //Флаг запуска опроса устройств
    volatile int iTrOn = 1;                         //Флаг выключения потока
    boolean bGetIpAuto = false;
    short ipOktetStart1 = 0;                        //Октеты стартового IP адреса
    short ipOktetStart2 = 0;
    short ipOktetStart3 = 0;
    short ipOktetStart4 = 0;
    short ipOktetEnd1 = 0;                          //Октеты конечного IP адреса
    short ipOktetEnd2 = 0;
    short ipOktetEnd3 = 0;
    short ipOktetEnd4 = 0;
    short ipOktetCur1 = 0;                          //Октеты текущего IP адреса
    short ipOktetCur2 = 0;
    short ipOktetCur3 = 0;
    short ipOktetCur4 = 0;
    short ipOktetMy1 = 0;                           //Октеты "моего" IP адреса
    short ipOktetMy2 = 0;
    short ipOktetMy3 = 0;
    short ipOktetMy4 = 0;
    boolean bEndStop;                               //Признак окончания формирования конечного IP адреса

    ScannerBean objScannerBean;
    Set<ListIp> listIp = null;

    public Thread_Scan(Settings settings, ScannerBean objScannerBean){

        this.objScannerBean = objScannerBean;

        Scanner_settings scannerSettings = gson.fromJson(settings.getScanner_settings(), Scanner_settings.class);
        Snmp_settings snmpSettings = gson.fromJson(settings.getSnmp_settings(), Snmp_settings.class);
        listIp = settings.getListIp();


        strMyIp = scannerSettings.getIp();
        discoveryNetworkPrefixLength = scannerSettings.getNetworkPrefixLength();
        discoveryGetNetworkPrefixLengthFromSettings = scannerSettings.isGetNetworkPrefixLengthFromSettings();
        discoveryNetScanPeriod = scannerSettings.getNetScanPeriod().intValue();
        waitingForPollingDevices = scannerSettings.getWaitingForPollingDevices().intValue();

        smnpPort = String.valueOf(snmpSettings.getSmnpPort());
        smnpTransportProtokol = snmpSettings.getSmnpTransportProtokol();
        smnpCOMMUNITY = snmpSettings.getSmnpCOMMUNITY();
        smnpRETRIES = snmpSettings.getSmnpRETRIES();
        smnpTIMEOUT = snmpSettings.getSmnpTIMEOUT();
        smnpVersion = snmpSettings.getSmnpVersion();

    }

    /*
 Запуск SNMP
 */
    private void startSNMP() throws IOException {
        try {
            transport = new DefaultUdpTransportMapping();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IOException in function 'startSNMP'", e);
        }
        snmp = new Snmp(transport);
        transport.listen();
        LOGGER.debug("Created SNMP oject");
    }

    /*
    Останов SNMP
    */
    private void stopSNMP() throws IOException {
        try {
            if (transport != null) {
                transport.close();
                transport = null;
            }

        } finally {
            if (snmp != null) {
                snmp.close();
                snmp = null;
                LOGGER.debug("discovery: object SNMP close");
            }
        }
    }

    /*
    Формирование структуры с данными об адрессате
    */
    private Target getTarget(String ip) {

        Address targetAddress = GenericAddress.parse(smnpTransportProtokol + ":" + ip + "/" + smnpPort);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(smnpCOMMUNITY));
        target.setAddress(targetAddress);
        target.setRetries(smnpRETRIES);
        target.setTimeout(smnpTIMEOUT);

        if(smnpVersion.equals("version1"))
            target.setVersion(SnmpConstants.version1);
        else if(smnpVersion.equals("version2c"))
            target.setVersion(SnmpConstants.version2c);
        else if(smnpVersion.equals("version3"))
            target.setVersion(SnmpConstants.version3);

        return target;

    }

    /*
    Останов потока опроса
    Закрывает SNMP и останавливает поток
    */
    public void stopThreadNetScan(){

        iTrOn = 0;
        try {
            stopSNMP();
        } catch (IOException e) {
            LOGGER.debug("discovery: error in function stopThread: " + e.getMessage());
        }

    }


    public void execute() {

        if(!createIpPool()){

            LOGGER.debug("Stop DISCOVERY scan");
            return;

        }

        try {
            startSNMP();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IOException in function 'execute'", e);
        }


        //Запускаем поток опроса устройств по протоколу SNMP
        thrd = new Thread(this);
        thrd.start();
        LOGGER.debug("Created SNMP thread");
    }

    public void run() {

        StringBuilder strIP = new StringBuilder();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.debug("*****    Start SCAN SNMP   *****");
        short cntThread = 0;

        while(iTrOn == 1){

            cntThread++;
            LOGGER.debug("*****    Start cycle SCAN  " + cntThread + "   *****");

            if ((listIp!=null)&&(listIp.size()>0)) {

                for(ListIp lIp : listIp){
                    sendSnmpRequest(lIp.getIp());
                    LOGGER.debug(lIp.getIp());
                }

            } else {

                ipOktetCur1 = ipOktetStart1;
                ipOktetCur2 = ipOktetStart2;
                ipOktetCur3 = ipOktetStart3;
                ipOktetCur4 = ipOktetStart4;
                boolean bEndScan = false;

                while (!bEndScan) {
                    strIP.append(ipOktetCur1 + "." + ipOktetCur2 + "." + ipOktetCur3 + "." + ipOktetCur4);
                    sendSnmpRequest(strIP.toString());
                    LOGGER.debug(strIP.toString());
                    bEndScan = !getNextIp();
                    /*
                    if ((ipOktetCur1 == ipOktetEnd1) && (ipOktetCur2 == ipOktetEnd2) && (ipOktetCur3 == ipOktetEnd3) && (ipOktetCur4 == ipOktetEnd4)) {
                        bEndScan = true;
                    } else {
                        getNextIp();
                    }
                    */
                    strIP.delete(0,strIP.length());
                }
            }

            LOGGER.debug("*****   Finish cycle SCAN  " + cntThread + "  *****");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Вычисляется следующий IP адрес из пула адресов.
    //"Свой" адрес исключается
    public boolean getNextIp(){

        boolean bComplect = false;
        boolean bRes = false;

        while(!bComplect) {
            ipOktetCur4++;
            if (ipOktetCur4 > 255) {
                ipOktetCur4 = 0;
                ipOktetCur3++;
                if (ipOktetCur3 > 255) {
                    ipOktetCur3 = 0;
                    ipOktetCur2++;
                    if (ipOktetCur2 > 255) {
                        ipOktetCur2 = 0;
                        ipOktetCur1++;
                        if(ipOktetCur1>255)
                            ipOktetCur1 = 0;
                    }
                }
            }

            if ((ipOktetCur1 == ipOktetEnd1) && (ipOktetCur2 == ipOktetEnd2) && (ipOktetCur3 == ipOktetEnd3) && (ipOktetCur4 == ipOktetEnd4)) {
                return false;
            }

            if((ipOktetCur1 == ipOktetMy1)&&(ipOktetCur2 == ipOktetMy2)&&(ipOktetCur3 == ipOktetMy3)&&(ipOktetCur4 == ipOktetMy4))
                bComplect = false;
            else
                bComplect = true;
        }
        return true; //ipOktetCur1 + "." + ipOktetCur2 + "." + ipOktetCur3 + "." + ipOktetCur4;

    }

    //Функция отправки SNMP-запроса на заданный адрес
    public void sendSnmpRequest(String ip){

        if(snmp != null) {

            Target target = getTarget(ip);
            PDU pdu = new PDU();
            pdu.setType(PDU.GET);
            pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0")));
            pdu.setNonRepeaters(1);

            try {
                snmp.send(pdu, target, null, this);
            } catch (IOException e) {
                LOGGER.error("ERROR in function 'sendSnmpRequest':" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /*
    * Ответ от устройства на запрос "GET"
    * */
    public void onResponse(ResponseEvent event) {

        if(event.getResponse() != null) {
            setResponseDataToCollection(event.getResponse(), event.getPeerAddress());
            //LOGGER.debug("OK in function onResponse: response to a request " + event.getRequest().getRequestID().toString());
        }
        //else
        //LOGGER.debug("WARNING in function onResponse: no response to a request " + event.getRequest().getRequestID().toString());
    }

    //Запись ответа от устройства в коллекцию
    public synchronized void setResponseDataToCollection(PDU responsePDU, Address adr) {

        if (responsePDU == null) {
            LOGGER.debug("WARNING in function setResponseDataToCollection: parameter 'responsePDU' is null.");
            return;
        }
        if (adr == null) {
            LOGGER.debug("WARNING in function setResponseDataToCollection: parameter 'adr' is null.");
            return;
        }

        if (responsePDU.getType() == PDU.RESPONSE) {

            String[] strAddr = null;
            strAddr = adr.toString().split("/");
            String description = responsePDU.getVariable(new OID("1.3.6.1.2.1.1.1.0")).toString();

            if(objScannerBean!=null){

                objScannerBean.saveDeviceInfo(strAddr[0], description);
            }
        }

    }
/*
    //Функция автоматического получения "своих" IP адреса и маски сети
    public boolean  getIpAuto(){

        //boolean bRes = true;
        bGetIpAuto = true;

        if(discoveryGetNetworkPrefixLengthFromSettings){
            return bGetIpAuto;
        }
        //Диапазон IP-адресов определяем JAVA-методом
        DatagramSocket s = null;
        try
        {
            s=new DatagramSocket();
            s.connect(InetAddress.getByAddress(new byte[]{1, 1, 1, 1}), 0);
            if(s.isConnected()) {
                NetworkInterface netint = NetworkInterface.getByInetAddress(s.getLocalAddress());
                if (netint == null) {
                    LOGGER.error("ERROR in function 'getIpAuto': netint is null");
                    bGetIpAuto = false;
                }
                List<InterfaceAddress> interfaceAddresses = netint.getInterfaceAddresses();
                if (interfaceAddresses == null) {
                    LOGGER.error("ERROR in function 'getIpAuto': interfaceAddresses is null");
                    bGetIpAuto = false;
                }

                strMyIp = "";


                if (interfaceAddresses.size() > 0) {
                    bGetIpAuto = false;
                    for (InterfaceAddress addr : interfaceAddresses) {

                        if(addr!=null) {
                            strMyIp = "";
                            String strIp = addr.getAddress().toString();
                            int cnt = 0;
                            for (int ind = 0; ind < strIp.length(); ind++) {
                                if ((Character.isDigit(strIp.charAt(ind))) || (strIp.charAt(ind) == '.')) {
                                    strMyIp = strMyIp + strIp.charAt(ind);
                                }
                            }
                            discoveryNetworkPrefixLength = addr.getNetworkPrefixLength();
                            LOGGER.debug("The result of the query network parameters. Ip: " + strMyIp + ",  NetworkPrefixLength: " + discoveryNetworkPrefixLength);
                            bGetIpAuto = true;
                        }
                    }
                    if(!bGetIpAuto){
                        LOGGER.error("ERROR in function 'getIpAuto': InterfaceAddress is null");
                    }
                }
            }

        } catch (SocketException e) {
            LOGGER.error("ERROR in function 'getIpAuto': " + e.getMessage());
            e.printStackTrace();
            bGetIpAuto = false;

        } catch (UnknownHostException e) {
            LOGGER.error("ERROR in function 'getIpAuto': " + e.getMessage());
            e.printStackTrace();
            bGetIpAuto = false;

        } finally {
            if(s != null) {
                s.disconnect();
                s.close();
            }
        }

        return bGetIpAuto;

    }
*/
    /*
    Функция создает пул адресов для сканирования
    Пул создается на основе "своего" IP адреса и маски подсети, получаемых из настроек или java-методами.
    */

    private boolean createIpPool(){
        String []strOktet = null;


        if(strMyIp.length()<7){
            LOGGER.error("ERROR in function 'createIpPool': bad IP address = " + strMyIp);
            return false;
        }

        LOGGER.debug("My IP address: " + strMyIp);
        LOGGER.debug("My NetworkPrefixLength: " + discoveryNetworkPrefixLength);

        strOktet = strMyIp.split("\\.");

        if(strOktet == null){
            LOGGER.debug("ERROR in function createIpDiapazone: strOktet is null");
            return false;
        }
        if((discoveryNetworkPrefixLength < 0)|| (discoveryNetworkPrefixLength > 31)) {
            LOGGER.debug("ERROR in function createIpDiapazone: bad NetworkPrefixLength");
            return false;
        }

        ipOktetMy1 = Short.parseShort(strOktet[0]);
        ipOktetMy2 = Short.parseShort(strOktet[1]);
        ipOktetMy3 = Short.parseShort(strOktet[2]);
        ipOktetMy4 = Short.parseShort(strOktet[3]);

        //Формируем маску
        long  iMask = 0;

        for (int ind = 0; ind< (32-discoveryNetworkPrefixLength); ind++){
            iMask = iMask + (long)Math.pow(2, ind);
        }

        short oktetMask1 = (short) (iMask>>24);
        short oktetMask2 = (short) ((iMask>>16) & 255);
        short oktetMask3 = (short) ((iMask>>8) & 255);
        short oktetMask4 = (short) (iMask & 255);


        //Определяем стартовый и конечный адреса
        ipOktetStart1 = (short) ((short) ipOktetMy1&(~oktetMask1));
        ipOktetStart2 = (short) ((short) ipOktetMy2&(~oktetMask2));
        ipOktetStart3 = (short) ((short) ipOktetMy3&(~oktetMask3));
        ipOktetStart4 = (short) ((short) ipOktetMy4&(~oktetMask4));

//        ipOktetEnd1 = (short) (ipOktetStart1|oktetMask1);
//        ipOktetEnd2 = (short) (ipOktetStart2|oktetMask2);
//        ipOktetEnd3 = (short) (ipOktetStart3|oktetMask3);
//        ipOktetEnd4 = (short) (ipOktetStart4|oktetMask4);

        ipOktetEnd1 = ipOktetStart1;
        ipOktetEnd2 = ipOktetStart2;
        ipOktetEnd3 = ipOktetStart3;
        ipOktetEnd4 = ipOktetStart4;

        ipOktetEnd4 = checkOktetEnd(ipOktetEnd4);
        if(!bEndStop) ipOktetEnd3 = checkOktetEnd(ipOktetEnd3);
        if(!bEndStop) ipOktetEnd2 = checkOktetEnd(ipOktetEnd2);
        if(!bEndStop) ipOktetEnd1 = checkOktetEnd(ipOktetEnd1);

        if(ipOktetStart4<253) ipOktetStart4++;
        //if(ipOktetEnd4>0) ipOktetEnd4--;

        boolean bPoolCorrect = false;
        if( ipOktetStart1<= ipOktetEnd1){
            if( ipOktetStart2<= ipOktetEnd2){
                if( ipOktetStart3<= ipOktetEnd3){
                    if( ipOktetStart4< ipOktetEnd4){
                        bPoolCorrect = true;
                    }
                }
            }
        }
        if(!bPoolCorrect){
            ipOktetEnd1 = ipOktetStart1;
            ipOktetEnd2 = ipOktetStart2;
            ipOktetEnd3 = ipOktetStart3;
            ipOktetEnd4 = ipOktetStart4;
            if(ipOktetEnd4<255) ipOktetEnd4++;
            else{
                LOGGER.debug("ERROR in function createIpDiapazone: bad address pool");
                return false;
            }
        }

        LOGGER.info("Start IP address: " + ipOktetStart1 + "." + ipOktetStart2 + "." + ipOktetStart3 + "." + ipOktetStart4);
        LOGGER.info("End IP address: " + ipOktetEnd1 + "." + ipOktetEnd2 + "." + ipOktetEnd3 + "." + ipOktetEnd4);
        return true;
    }

    short checkOktetEnd(short oktet){

        int oktetWork = oktet;
        int maskEnd = 1;

        for(short ind = 0; ind<8; ind++){

            int iCheck = oktetWork&maskEnd;
            if( iCheck == 0 ) {

                oktetWork = oktetWork|maskEnd;
                maskEnd = maskEnd<<1;

            } else {
                bEndStop = true;
                return (short)oktetWork;
            }
        }

        return (short)oktetWork;
    }
}
