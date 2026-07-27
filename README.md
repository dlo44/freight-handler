# Freight Handler
RESTful Spring Boot application, serves as backend for Freight Handler Android app.

### About project
- My inspiration for this project was my current job where I work as a freight handler loading/ unloading trailers. This project is built from a previous project template. It serves as the back end for the Freight Handler Android application.
### Features
- Create, update, and delete doors, trailers, and shipments.
- Kafka/ Debezium integration to send data to the Android app in real time. This triggers a "busy" notification when a set amount of time has passed for a shipment waiting to be loaded 

### Technologies Used
- Java
- Spring boot
- Postgresql
- docker
- Debezium/ kafka

### Getting Started
1. Follow the instructions below under the "cs-3250" section to set up the docker container and postgres db.
2. Copy `src/main/resources/application-local.properties.example` to `src/main/resources/application-local.properties` before running the app.
3. Once the Application is running locally you can use postman to test the application.
4. Use POST request to create a Door, Trailer, and Shipment.
    - create one trailer, and door with status "UNLOAD", and one of each with status "LOAD".
    - create one shipment matching a "Destination" of a trailer for the suggestion feature in the android app.
5. Use PUT request to put trailer at door and shipment on trailer.
6. Use GET request to verify your previous request's. In order to properly simulate the workflow on the android app all 3 must exist.

### Run the Android App
- With the Spring Boot app now running you can run the Android app and see the data from the request in the Android app.

### Future Improvements
- This is a simplified version of my current workflow, and I would like to add more functionality in future versions.

# cs-3250
A starter template for our RESTful Spring Boot applications in CS 3250, Software Development Methods and Tools

### Running the Docker containers
1. You can install Docker on your computer if you have not already done so here: https://docs.docker.com/get-docker/
    * **NOTE for Windows users**: I *highly* recommend that you enable the Windows Subsystem for Linux Version 2 (WSL2), and install a default Linux distribution (i.e. Ubuntu 20.04) prior to installing Docker. Docker will run much easier on all editions of Windows 10 if you perform this step first
    * This article will walk you through enabling WSL2: https://www.omgubuntu.co.uk/how-to-install-wsl2-on-windows-10
        - **NOTE**: If your BIOS does not have virtualization enabled, you may encounter an error enabling WSL 2. This article can help you enable virtualization on your computer: https://support.bluestacks.com/hc/en-us/articles/115003174386-How-to-enable-Virtualization-VT-on-Windows-10-for-BlueStacks-4
    * Once you have Docker installed, set Ubuntu 20.04 to be your default distribution
        - First, make sure you have downloaded Ubuntu 20 from the Microsoft Store: https://www.microsoft.com/en-us/p/ubuntu-2004-lts/9n6svws3rx71
        - Next, open a command prompt as administrator (https://www.howtogeek.com/194041/how-to-open-the-command-prompt-as-administrator-in-windows-8.1/) and running the following command:
            - `wsl -s Ubuntu-20.04`
    * Once your default distribution is set, verify Docker is configured correctly to use WSL 2 and your default WSL distro: https://docs.docker.com/docker-for-windows/wsl/
        - Check the "Settings > General" and ""Settings > Resources > WSL Integration" sections of your Docker installation and compare them to the screenshots on this website
    * **NOTE**: On macOS you may want to increase the amount of memory available to Docker from 2GB to at least 5GB. If you go to Docker settings you can find the memory settings under Preferences -> Resources

2. After installing Docker, start the necessary containers for the project by running the `start-container` script appropriate for your operating system inside the `scripts` folder
    - `start-container` for macOS and Linux
    - `start-container.bat` for Windows

### Developing

1. Once the Docker containers have started, attach to the development container by running the `attach-container` script appropriate for your operating system inside the `scripts` folder
    - `attach-container` for macOS and Linux
    - `attach-container.bat` for Windows
2. If you want to attach to the Postgres container instead (necessary for week two lectures!), run the `attach-postgres-container` script
    - `attach-postgres-container` for macOS and Linux
    - `attach-postgres-container.bat` for Windows
3. Once inside the **start-container** script, you can run the following commands to build and test the project
    - `mvn compile` - Compile the application
        - NOTE: In order for Intellij to automatically reload Maven changes, I would encourage you to change your Maven auto-reload settings to "Any changes":
            - https://www.jetbrains.com/help/idea/delegate-build-and-run-actions-to-maven.html#auto_reload_maven
    - `mvn test -P test` - Run unit/integration tests
    - `mvn package spring-boot:repackage -P test` - Package the deployable JAR file for the application
    - `mvn spring-boot:run -Dspring-boot.run.profiles=local` - Start the Spring Boot application locally for testing
        - This will run until you press CTRL+C
        - NOTE: When you are running the application, it will automatically reload changes after the project is rebuilt! In order to take advantage of this, I would encourage you to configure auto-build in Intellij:
            - https://www.jetbrains.com/help/idea/compiling-applications.html#auto-build
4. NOTE: The **first time** you run the **start-container** script, you will need to run a few instructions to configure git so that you can push and pull your changes without having to log in each time:
    - `git config --global credential.helper store`
    - `git config --global push.default simple`
    - `git config --global user.name "Your Name"`
        - Replace "Your Name" with your actual contact information
    - `git config --global user.email user@msudenver.edu`
        - Replace "user@msudenver.edu" with your actual contact information
    - `git config --global core.eol lf`
    - `git config --global core.autocrlf input`
5. If at any point you need to re-create the 7dbs database we were using for lab03, I have provided a database export you can quickly import to get up and running! Here are the steps that are required:
    - Open a new terminal and run `attach-postgres-container` inside the `scripts` folder
    - Become the `postgres` user by running `su - postgres`
    - Import the database by running the commands:
        - `dropdb 7dbs`
        - `createdb 7dbs`
        - `createdb 7dbs_test`
        - `psql -d 7dbs < /app/postgres/database/7dbs.sql`
            - This command will take a few seconds to complete
    - After the import has completed, verify the `7dbs` database is present by running `psql 7dbs`
        - You can query the tables to make sure they have data: `SELECT * from events;`
6. In order to test your RESTful APIs manually, a set of Postman tests will be maintained in the `scripts/postman` folder. You can import these tests into your local Postman instance, modify them, and then update them in the repository! Instructions for how to do that can be found here:
    - https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman
    - https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#exporting-postman-data
    - https://www.youtube.com/watch?v=FzPBDU7cB74&ab_channel=FunDooTesters
7. We also have API Documentation generated automatically by Spring Boot itself, courtesy of Swagger and Swagger UI! To look at the APIs you have defined, and try them out as well, go to:
    - http://localhost:8080/swagger-ui/

### Deploying
> Not currently in use for this project — it is running locally only. Instructions below are kept for future reference.

1. We will be deploying our Spring Boot applications to Heroku, a cloud provider that knows how to natively run Spring Boot applications. There are a few **one-time** setup steps required to make sure your application goes to the right place, and they are summarized here:
    - First, you need to make sure your local environment is authenticated with Heroku. Do that by running the command `heroku login` from inside the `attach-conatiner` environment. **This command should only have to be executed once.**
        - This will provide you with a URL you need to copy and paste into a web browser to log in and authenticate with
    - Second, we have to make sure your Heroku account has subscribed to the Eco plan that is covered by your GitHub Student Pack. To subscribe to the Eco plan in Heroku, visit the following website for instructions: https://devcenter.heroku.com/articles/eco-dyno-hours
    - Third, you need to create an application inside Heroku to push your application to. This is accomplished by running the command `heroku create appname`, where `appname` is the name of the application you are creating
        - For labs, use the name `cs-3250-lab#-username`, where `lab#` is the lab number (like lab04), and username is your GitHub username, for example `heroku create cs-3250-lab04-dpittman`. **This command should only have to be executed once.**
    - Fourth, we need to add a free tier PostgreSQL database to our deployment. We can do this by running the command `heroku addons:create heroku-postgresql:mini`. **This command should only have to be run once.**
    - Fifth, we need to change the Maven command that is executed to build and package our application. We can do this by running the command `heroku config:set MAVEN_CUSTOM_GOALS="clean dependency:list package spring-boot:repackage install"`. **This command should only have to be run once.**
2. Once you have run all the one-time setup steps for your repository, you can then build and deploy your app to Heroku with the following commands:
    - `git add .`
    - `git commit -m "Deployment to Heroku"`
    - `git push heroku main`
3. To get the URL by which you can access your Spring Boot API, run the command `heroku open` and it will display the appropriate URL
4. If you need to check the logs for your Spring Boot application, you can run the command `heroku logs` to see any errors that might have occurred
5. If you need to force Heroku to rebuild your application, you can do so by creating an "empty commit" via this command: `git commit --allow-empty -m "empty commit"`. You can then push the empty commit to cause Heroku to rebuild with `git push heroku main`
