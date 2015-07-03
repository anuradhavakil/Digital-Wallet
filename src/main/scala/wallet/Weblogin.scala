package wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.mongodb.core.index.Indexed

import scala.annotation.meta.field

/**
 * Created by Anu on 9/22/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class Weblogin(@(Indexed@field)(unique = true)webloginId : String,userId : String,url : String, login : String, password : String)

