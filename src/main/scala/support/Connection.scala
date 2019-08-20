package support

import scalikejdbc.{ConnectionPool, ConnectionPoolSettings}

trait Connection {
  Class.forName("org.h2.Driver")
  val settings = ConnectionPoolSettings(
    initialSize = 0,
    maxSize = 8,
    connectionTimeoutMillis = 5000L,
    validationQuery = "select 1 as one",
    connectionPoolFactoryName = null,
    driverName = "org.h2.Driver",
    warmUpTime = 100L,
    timeZone = "UTC"
  )
  ConnectionPool.singleton("jdbc:h2:mem:default", "", "", settings)
}

import scalikejdbc._
import skinny.dbmigration.DBSeeds

//noinspection SqlNoDataSourceInspection
trait DatasourceConfig extends DBSeeds {

  addSeedSQL(
    sql"""
         create table contact (
           id bigint(20) not null auto_increment primary key,
           name varchar(50) not null unique
         ) engine=innoDB
         """,
    sql"""
         create table role (
           id bigint(20) not null auto_increment primary key,
           name varchar(50) not null unique,
           contact_id bigint(20) not null
         ) engine=innoDB
         """,
    sql"""
         create table phone (
           id bigint(20) not null auto_increment primary key,
           number int(20) not null
         ) engine=innoDB;
         """,
    sql"""
         create table role_phone (
           role_id bigint(20) not null,
           phone_id bigint(20) not null
           ) engine=innoDB;
         """,
    sql"""
          create table person_with_nickname (
          id bigint(20) not null,
          nickname varchar(200)
          ) engine=innoDB
      """
  )

  runIfFailed(sql"""select count(1) from contact""")
}