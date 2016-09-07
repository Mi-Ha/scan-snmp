package example.group.sd.data.run;

import example.group.sd.data.config.ConfigurationBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DropCreateDatabaseScheme {

    static final Logger LOG = LoggerFactory.getLogger(DropCreateDatabaseScheme.class);

    public DropCreateDatabaseScheme() {
        LOG.info("constructing DropCreateDatabaseScheme");
    }



    public static void main(String[] args)
    {
        LOG.info("invoke DropCreateDatabaseScheme.main");
        {
            System.setProperty("rdbms.jpa.hibernate.ddl-auto", "create-drop");
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(ConfigurationBase.class);
            ctx.refresh();
        }
        {
            System.setProperty("rdbms.jpa.hibernate.ddl-auto", "create");
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(ConfigurationBase.class);
            ctx.refresh();
        }
    }
}
