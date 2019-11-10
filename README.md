# movieManager
A RESTful API application that manages a personal movie collection

## Attention:
Jackson v 2.9.7 has a vulnerability referenced in the following CVE:

CVE-2019-16942 - https://github.com/advisories/GHSA-mx7p-6679-8g3q

Going to v 2.10.1 breaks the ArrayNodeCollector implementation used and would 
require more time to investigate that is allowed for this ask. 

## Setting up your system
After completing these steps, you will have your system setup for development.

1. Install a Java 11 JDK

    This project requires at least Java 11.

    **Ubuntu 18.04**
    
    ```bash
    $ sudo add-apt-repository ppa:openjdk-r/ppa
    $ sudo apt install openjdk-11-jdk openjdk-11-source
    ```

    **Mac and Windows**
    
    You can install the Oracle JDK from here:
    https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html    

2. Install Docker and Docker Compose

    Follow these instructions:
    * Ubuntu
      * Docker - https://docs.docker.com/install/linux/docker-ce/ubuntu/
      * Docker Compose - https://docs.docker.com/compose/install/
    * Mac - https://docs.docker.com/docker-for-mac/install/
    * Windows - https://docs.docker.com/docker-for-windows/install/
    
## Run the Application

1. Start the required services

    **Running on Docker**

    This will start a local Undertow AppServer using Docker.
    It will default to running everything locally via port 9001.
    ```bash
    $ make docker/build
   $ make docker/run
   ```
   
 **Running on local machine**
 
   This will build and run the application natively on your local system
   ```bash
   $ make run
   ```

## Things of Note:
The requirement here is to have a production ready API. Production is a bit ambitious
to get done in two days.  With that said I feel this implementation while
there are some bugs it can stand up to some amount of rigor. 

The API does not have security or and throttling/backpressure technology. However I wanted to 
honor the request for production ready code and focus most of my time making this 
solution as solid as possible.  


