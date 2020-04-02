# scarif-starwars
Challenge project made for the b2w selection process: Micro-service, Dropwizard, hk2, testcontainer, swagger, mongodb.

**<h3>For running the application locally</h3>**
- Run the MongoDB container start script
- If docker is not going to be used, edit bank connection properties in config.yml
    - repository:
        - host: localhost
        - port: 27017
        - user: user
        - passwd: mypass
        - db: starwars
        - collectionName: planetas

**<h3>For the execution of unit tests</h3>**
>Mandatory Docker installation
