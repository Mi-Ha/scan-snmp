# Test Spring Data Rest via commandline (got info from [https://github.com/spring-projects/rest-shell](https://github.com/spring-projects/rest-shell) )

## Start tomcat test server
		
		mvn tomcat7:run		
		
## (Spring Rest Shell)  Building and Running

		git clone git://github.com/spring-projects/rest-shell.git
		cd rest-shell
		./gradlew installApp
		cd build/install/rest-shell-1.2.0.RELEASE
		bin/rest-shell

## Connection and playing (from rest-shell) ; after # - curl equivalent

        # --- get list ---
        discover http://localhost:9090
        follow items
        discover         
        # curl -v http://localhost:9090/items/
        
        # --- make query ---
        get search/findWithFieldValue --params "{field_type_alias: 'NAME', value: 'test network item'}"
        # curl http://localhost:9090/items/search/findWithFieldValue?field_type_alias=NAME&value=test+network+item        
        follow 36
        discover 
        follow fields
        get
        up # go to root
        
        # --- change ---
        follow itemFields
        follow 37
        post --data "{value: 'newValue123'}" #
        # curl -v -X PATCH -H "Content-Type:application/json" -d '{"value": "newValue123"}' http://localhost:9090/itemFields/37
        get
        # curl -v http://localhost:9090/itemFields/37
        
        # --- delete ---
        discover http://localhost:9090
        follow items
        delete 51        
        # curl -v -X DELETE http://localhost:9090/items/51

# Test Spring Rest 

## Start tomcat test server
		
		mvn tomcat7:run		

## Play in browser

Go to http://localhost:9090/swagger-ui.html , on 'CenterMind REST service' click 'List Operations'. Now, in opened list, you can invoke methods.
Also, you also will see Curl command after invoke it.

