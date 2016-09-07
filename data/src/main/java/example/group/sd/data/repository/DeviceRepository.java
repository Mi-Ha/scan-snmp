package example.group.sd.data.repository;

import example.group.sd.data.entity.Device;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends CrudRepository<Device, Long> {

    //@Transactional
    @Query("select i from Device i")
    public List<Device> getAllDevices();

    //@Transactional
    public List<Device> findByIp(String ip);
	
}
