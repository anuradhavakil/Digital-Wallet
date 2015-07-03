package wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonCreator

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import scala.annotation.meta.field
import scala.beans.BeanProperty

/**
 * Created by Anu on 9/20/2014.
 */

@JsonCreator
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "user")
case class User(@(Indexed@field)(unique = true)userId : String , email: String, password: String, created_at: String, updated_at: String)