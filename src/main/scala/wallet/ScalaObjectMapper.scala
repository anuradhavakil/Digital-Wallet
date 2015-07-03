package wallet


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule





/**
 * Created by Anu on 9/20/2014.
 */
class ScalaObjectMapper extends ObjectMapper{
  registerModule(DefaultScalaModule)
}
