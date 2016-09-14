package example.group.sd.scanwar.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages={
        "example.group.sd.rest.controller"
        , "example.group.sd.scanner"
        //, "example.group.sd.rest.data"
})
@EnableWebMvc
public class ConfigMVC extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //http://localhost:8080/index.html
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/frontend/");
    }
}