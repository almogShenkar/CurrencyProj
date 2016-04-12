package com.almog.project
import java.net._

import com.almog.project.ModelInterFace
import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator;

// this class implement the ModelInterFace
class Model() extends ModelInterFace {

  val url = new URL("http://www.boi.org.il/currency.xml")  //returns URL object
  val conn = url.openConnection              //returns URLConnection object
  val doc = XML.load(conn.getInputStream)    //returns Elem object represent XML element

  def getDate(): String = (doc \\ "LAST_UPDATE").text //return date of last update

  def getData(): Array[String] = {

    var vec: Array[String] = new Array[String](28)
    var i: Int = 0

    for (elm <- (doc \\ "CURRENCY")) { //parse the xml currencies, return data as an array
      var rate: Double = 0
      rate = (elm \\ "RATE").text.toDouble
      if ((elm \\ "UNIT").text.toDouble == 10) {
        rate = rate / 10
      }
      if ((elm \\ "UNIT").text.toDouble == 100) {
        rate = rate / 100
      }
      vec(i) = ((elm \\ "COUNTRY").text)
      i += 1
      vec(i) = rate.toString()
      i += 1
    }
    vec
  }

  def Convert(from: String, amount: Double, to: String): Double = {
    var fromRate: Double = 1
    var toRate: Double = 1
    var shekel: Double = 1
    var Result: Double = 0

    breakable {
      for (elm <- (doc \\ "CURRENCY")) {
        if (from == "ILS") { //if shekel then No-change
          fromRate = 1
          break
        }
        if (from == (elm \\ "CURRENCYCODE").text) { //if any "regular" coins
          fromRate = ((elm \\ "RATE").text.toDouble)
          break
        }
      }
    }
    if (from == "JPY") //JPY & LBP fix "to small" rates from 1 unit
      fromRate = fromRate / 100
    if (from == "LBP")
      fromRate = fromRate / 10

    breakable {
      for (elm <- (doc \\ "CURRENCY")) {
        if (to == (elm \\ "CURRENCYCODE").text) {
          toRate = ((elm \\ "RATE").text.toDouble)
          break;
        }
      }
    }

    if (from == "JPY")
      fromRate = fromRate / 100 //JPY & LBP fix "to small" rates from 1 unit
    if (from == "LBP")
      fromRate = fromRate / 10

    if (from != "ILS") { //calculate result
      shekel = fromRate * amount
      Result = shekel / toRate
    }

    if (from == "ILS") {
      Result = amount / toRate
    }
    Result
  }


}

