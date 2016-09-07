package example.group.sd.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import example.group.sd.data.utils.FieldConstEx;
import example.group.sd.data.utils.JPAConst;

@Entity(name="Settings")
@Table(name="SETTINGS")
public class Settings {

	private Long id;
	private String alias;
	private String snmp_settings;
	private String scanner_settings;
	private Set<ListIp> listIp;
	
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
	
	@Column(name = FieldConstEx.ALIAS, unique = false,length = 128)
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Lob
	@Column(name = FieldConstEx.SNMP_SETTINGS)
	public String getSnmp_settings() {
		return snmp_settings;
	}
	public void setSnmp_settings(String snmp_settings) {
		this.snmp_settings = snmp_settings;
	}

	@Lob
	@Column(name = FieldConstEx.SCANNER_SETTINGS)
	public String getScanner_settings() {
		return scanner_settings;
	}
	public void setScanner_settings(String scanner_settings) {
		this.scanner_settings = scanner_settings;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "settings", cascade = CascadeType.REMOVE)
	//@NotFound(action = NotFoundAction.IGNORE)
	public Set<ListIp> getListIp() {
		
		if (listIp == null)
			listIp = new HashSet<ListIp>();
		
		return listIp;
	}
	public void setListIp(Set<ListIp> listIp) {
		this.listIp = listIp;
	}
	
	
	
	
}
