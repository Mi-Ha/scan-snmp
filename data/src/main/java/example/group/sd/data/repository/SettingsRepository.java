package example.group.sd.data.repository;


import org.springframework.data.repository.CrudRepository;
import example.group.sd.data.entity.Settings;
import org.springframework.transaction.annotation.Transactional;

public interface SettingsRepository extends CrudRepository<Settings, Long> {
	@Transactional
	Settings findByAlias(String alias);
}
