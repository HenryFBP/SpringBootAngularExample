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
