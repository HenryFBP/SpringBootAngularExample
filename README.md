# Spring Boot Angular Example

taken from amigoscode with <3

<https://www.youtube.com/watch?v=8ZPsZBcue50>

<https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.7.1&packaging=jar&jvmVersion=17&groupId=net.henrypost&artifactId=server&name=server&description=server%20manager%20app&packageName=net.henrypost.server&dependencies=web,mysql,validation,data-jpa,lombok>


## Running

### docker (for le database)

    docker-compose up -d

    # create db
    docker-compose exec postgres createdb --username=amigoscode serverdb

    # optional commands below
    docker-compose ps
    docker-compose logs -f

Then visit <http://localhost:5050/> to view PGAdmin.

Note the hostname is "postgres" (coming from the docker network) - not localhost.

Login is `amigoscode:password`.
