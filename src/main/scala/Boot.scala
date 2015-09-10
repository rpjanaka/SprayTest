package com.scala.spray.test

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.ConfigFactory
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.util.Try

/**
 * Created by janaka on 9/10/15.
 */
object Boot extends App {

  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8080)
  lazy val serviceHost = Try(config.getInt("service.host")).getOrElse("localhost")
  // create our actor system with the name smartjava
  implicit val system = ActorSystem("smartjava")
  val service = system.actorOf(Props[RestServiceActor], "rest-service-actor")
  //loading config
  val config = ConfigFactory.load()
  println(serviceHost + ":" + servicePort)
  // IO requires an implicit ActorSystem, and ? requires an implicit timeout
  // Bind HTTP to the specified service.
  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8081)
}
