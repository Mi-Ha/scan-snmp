package example.group.sd.rest.controller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages={
        "example.group.sd.rest.controller",
        "example.group.sd.data"
})
@EnableWebMvc
//@Import(SwaggerConfiguration.class)

public class ConfigMVC extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
//
        registry.addResourceHandler("/webjars/**") // springfox-swagger-ui
                .addResourceLocations("classpath:/META-INF/resources/webjars/");


    }
}