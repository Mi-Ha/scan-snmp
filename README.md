### Настройка Tomcat JNDI для конфигурации приложения при старте.

В conf/context.xml нужно прописать следующее:

```xml

    <Resource name="jdbc/centermind" auth="Container"
              type="javax.sql.DataSource" driverClassName="org.postgresql.Driver"
              url="jdbc:postgresql://<db_server_ip>/<db_name>"
              username="<db_username>" password="<db_password>" maxActive="20" maxIdle="10"
    maxWait="-1"/>

```


В conf/web.xml нужно прописать следующее:

```xml

     <resource-ref>
          <description>postgreSQL CenterMind datasource</description>
          <res-ref-name>jdbc/centermind</res-ref-name>
          <res-type>javax.sql.DataSource</res-type>
          <res-auth>Container</res-auth>
     </resource-ref>

```
