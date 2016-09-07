package example.group.sd.data.utils;

public class Scanner_settings {
	String ip;
	int networkPrefixLength;
	boolean getNetworkPrefixLengthFromSettings;
	Long netScanPeriod;
	Long sendWorkingEvent;
	Long waitingForPollingDevices;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getNetworkPrefixLength() {
		return networkPrefixLength;
	}
	public void setNetworkPrefixLength(int networkPrefixLength) {
		this.networkPrefixLength = networkPrefixLength;
	}
	public boolean isGetNetworkPrefixLengthFromSettings() {
		return getNetworkPrefixLengthFromSettings;
	}
	public void setGetNetworkPrefixLengthFromSettings(
			boolean getNetworkPrefixLengthFromSettings) {
		this.getNetworkPrefixLengthFromSettings = getNetworkPrefixLengthFromSettings;
	}
	public Long getNetScanPeriod() {
		return netScanPeriod;
	}
	public void setNetScanPeriod(Long netScanPeriod) {
		this.netScanPeriod = netScanPeriod;
	}
	public Long getSendWorkingEvent() {
		return sendWorkingEvent;
	}
	public void setSendWorkingEvent(Long sendWorkingEvent) {
		this.sendWorkingEvent = sendWorkingEvent;
	}
	public Long getWaitingForPollingDevices() {
		return waitingForPollingDevices;
	}
	public void setWaitingForPollingDevices(Long waitingForPollingDevices) {
		this.waitingForPollingDevices = waitingForPollingDevices;
	}

	
}
