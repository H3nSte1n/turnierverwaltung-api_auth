# Turnierverwaltung - Authentification

![CI](https://github.com/H3nSte1n/turnierverwaltung-api_auth/workflows/CI/badge.svg) ![coverage](https://github.com/H3nSte1n/turnierverwaltung-api_auth/blob/main/.github/badges/jacoco.svg)


## setup

### required
- postgreSQL database

> *If you want to use another database, you have adapt the dataSourceClassName in config/DatabaseFactory*

### database
    createdb -h localhost -U {USERNAME} -W {DATABASE}
    createdb -h localhost -U {USERNAME} -W {DATABASE-TEST}

### application
    gradle run

### ENV
- Removing the dist wording from the .env(.test).dist filename\
***.env(.test).dist -> .env(.test)***


- set following env variables
  - USER=db-username
  - PASSWORD=db-password
  - DATABASE=db-name
  - PORT=port
  - SERVER=server

