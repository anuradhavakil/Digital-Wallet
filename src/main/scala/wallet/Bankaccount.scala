package wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.mongodb.core.index.Indexed

import scala.annotation.meta.field

/**
 * Created by Anu on 9/23/2014.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
case class Bankaccount(@(Indexed@field)(unique = true)bankaccountId : String,userId : String,account_name: String, routing_number: String, account_number : String)
