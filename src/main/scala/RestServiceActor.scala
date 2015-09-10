/**
 * Created by janaka on 9/10/15.
 */

package com.scala.spray.test

import akka.actor.Actor
import akka.event.Logging
import com.scala.spray.test.MyJsonProtocol._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.routing.authentication.BasicAuth

import scala.concurrent.ExecutionContext


// simple actor that handles the routes.
class RestServiceActor extends Actor with HttpService {

  val log = Logging(context.system, this)
  // handles the api path, we could also define these in separate files
  // this path respons to get queries, and make a selection on the
  // media-type.
  val aSimpleRoute = {
    log.info("request received: aSimpleRoute")

    authenticate(basic_authenticator) { userName =>
      log.info("Request authontication done...!")
      path("path1") {
        log.info("request received: aSimpleRoute, path1")


        get {
          log.info("request received: aSimpleRoute2, path1, get")

          //authorize(userName.hasReadingAccess) {

          respondWithMediaType(`application/json`) {
            complete {
              log.info("writing resp")
              log.info("------------------------")
              Person("Name", "Type Get", System.currentTimeMillis());
            }
          }
          //}

        } ~ post {
          respondWithMediaType(`application/json`) {
            complete {
              log.info("writing resp")
              log.info("------------------------")
              Person("Bob", "Type Post", System.currentTimeMillis());
            }
          }
        }
      }
    }
  }
  // handles the other path, we could also define these in separate files
  // This is just a simple route to explain the concept
  val anotherRoute = {
    log.info("1; request recieved: anotherRoute")
    path("path2") {
      log.info("2; request recieved: anotherRoute")
      get {
        log.info("3; request recieved: anotherRoute")
        // respond with text/html.
        respondWithMediaType(`text/html`) {
          complete {
            // respond with a set of HTML elements
            <html>
              <body>
                <h1>Path 2</h1>
              </body>
            </html>
          }
        }
      }
    }
  }

  //def receive = runRoute(aSimpleRoute)

  // required as implicit value for the HttpService
  // included from SJService
  def actorRefFactory = context

  // we don't create a receive function ourselve, but use
  // the runRoute function from the HttpService to create
  // one for us, based on the supplied routes.
  def receive = runRoute(aSimpleRoute ~ anotherRoute)

  ///authontication given in the config file src/main/resources/application.conf
  implicit def ec: ExecutionContext = actorRefFactory.dispatcher

  def basic_authenticator = BasicAuth("Pagero Biller Gateway")

}