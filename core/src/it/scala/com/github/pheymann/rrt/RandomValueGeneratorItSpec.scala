package com.github.pheymann.rrt

import com.github.pheymann.rrt.TestAction.{DoubleData, GetTestCase, IntData, PositiveIntData, StaticData}
import org.specs2.mutable.Specification

class RandomValueGeneratorItSpec extends Specification {

  sequential

  val testName = "random-value-it-spec"
  val testConfig = newConfig(testName, "127.0.0.1", 9000, "127.0.0.1", 9001).withRepetitions(10)

  "The library api" should {
    "provide an action to select values from a given static `List` randomly" in new WithTestServices(testConfig) {
      val testCase = for {
        statics <- staticData("Luke", "Boba", "Yoda", "Anakin", "Han", "C3PO")
        result  <- testGet { _ =>
          val uri = s"/hello/${statics()}"

          |+|(uri)
        }
      } yield result

      checkAndLog(testCase.runCase(testConfig)) should beTrue
    }

    "provide an action to create random `Int` and positive `Int` generators" in new WithTestServices(testConfig) {
      val testCase = for {
        ints          <- intData(Some(10))
        positiveInts  <- positiveIntData()
        result        <- testGet { rand =>
          val uri = s"/add/${ints()}/and/${ints()}"
          val params = rand.nextOptPair("offset", positiveInts)

          uri |+| params
        }
      } yield result

      checkAndLog(testCase.runCase(testConfig)) should beTrue
    }

    "provide an action to create random `Double` generators" in new WithTestServices(testConfig) {
      val testCase = for {
        doubles <- doubleData(Some(10.0))
        result  <- testGet { _ =>
          val uri = s"/multiply/${doubles()}/and/${doubles()}"

          |+|(uri)
        }
      } yield result

      checkAndLog(testCase.runCase(testConfig)) should beTrue
    }
  }

}
