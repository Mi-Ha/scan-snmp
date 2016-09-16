package example.group.sd.rest.base;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.springframework.web.context.request.RequestContextListener;

public abstract class TestRestBase extends JerseyTest {

	protected abstract Class<?>[] getServiceClasses();

	protected abstract String getSpringConfigLocation();

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		ResourceConfig cfg = new ResourceConfig(getServiceClasses());
		cfg.register(SpringLifecycleListener.class);
		cfg.register(RequestContextListener.class);
		cfg.property("contextConfigLocation", getSpringConfigLocation());

		return cfg;
	}

}