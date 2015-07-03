package wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.mongodb.core.index.Indexed

import scala.annotation.meta.field
import scala.beans.BeanProperty

/**
 * Created by Anu on 9/21/2014.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
case class Card(@(Indexed@field)(unique = true)cardId : String,userId : String,card_name:String, card_number:String, expirationDate:String)