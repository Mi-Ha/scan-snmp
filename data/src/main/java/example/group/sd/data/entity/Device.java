package example.group.sd.data.entity;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import example.group.sd.data.utils.FieldConstEx;
import example.group.sd.data.utils.JPAConst;

import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.Set;
import java.util.UUID;


@Entity(name = "Device")
@Table(name = "DEVICE")
public class Device {

	private Long id;
	
	private String ip;
	private String description;
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	//@Type(type= JPAConst.PGUUID)
	//@Column(name = FieldConstEx.ID, unique = true, nullable = false)


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

	@Column(name = "ip", unique = false,length = 128)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	//@Lob
	@Column(name = "description", unique = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
