package example.group.sd.scanwar.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages={"example.group.sd.rest.controller", "example.group.sd.rest.data"})
@EnableWebMvc
@Import(SwaggerConfiguration.class)

public class ConfigMVC extends WebMvcConfigurerAdapter {
//    @Autowired
//    private UsefulBean useful;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**") // springfox-swagger-ui
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        //http://localhost:8080/app/index.html
        //\center-mind\center-mind\scan-war\src\main\resources\app
        registry.addResourceHandler("/app/**")
                .addResourceLocations("classpath:/app/");

        //http://localhost:8080/integration/index.html
        //\center-mind\center-mind\integration\src\main\resources\integration
        registry.addResourceHandler("/integration/**")
                .addResourceLocations("classpath:/integration/");

        //http://localhost:8080/topology/html/index.html
        //\center-mind\center-mind\topology\src\main\resources\topology\html
        registry.addResourceHandler("/topology/**")
                .addResourceLocations("classpath:/topology/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/frontend/");


        //useful.createClusters();
    }
}