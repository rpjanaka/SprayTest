/**
 * Created by janaka on 9/10/15.
 */
package com.scala.spray.test

import spray.json.DefaultJsonProtocol

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val personFormat = jsonFormat3(Person)
  implicit val httpReqFormat = jsonFormat2(MyHttpRequest)
}

case class Person(name: String, fistName: String, age: Long)

case class MyHttpRequest(method: String, ip: String)
