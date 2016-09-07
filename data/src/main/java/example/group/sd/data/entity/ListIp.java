package example.group.sd.data.entity;

import javax.persistence.*;

import example.group.sd.data.utils.FieldConstEx;
import example.group.sd.data.utils.JPAConst;

@Entity(name="ListIp")
@Table(name="LIST_IP")
public class ListIp {

	private Long id;
	private Settings settings;
	private String ip;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@org.hibernate.annotations.Type(type = JPAConst.PGUUID)
    @Column(name = FieldConstEx.ID, unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FieldConstEx.SETTINGS_ID)
	public Settings getSettings() {
		return settings;
	}
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	
	@Column(name = FieldConstEx.IP, unique = false,length = 128)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
