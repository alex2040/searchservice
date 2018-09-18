#!/bin/bash -x
docker rm -f mysql
docker run -d --rm -p 3306:3306 \
 -e MYSQL_ROOT_PASSWORD=masterkey \
 -e MYSQL_DATABASE=search \
 -e MYSQL_USER=user \
 -e MYSQL_PASSWORD=pwd123 \
 -v /`readlink -f ../../sql/ddl.sql`:/docker-entrypoint-initdb.d/ddl.sql \
 --name mysql -d mysql:5.7 \
 mysqld --max-connections=100 --sql-mode=""