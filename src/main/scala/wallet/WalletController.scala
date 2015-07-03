package wallet

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util
import javax.servlet.http.HttpServletResponse

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.{Update, Query, Criteria}
import org.springframework.http.HttpStatus
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
;
import org.springframework.web.context.request;


import java.util.{Date, Calendar}
import javax.validation.Valid
import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.{NotEmpty, Email}
import org.springframework.web.context.request.WebRequest

import scala.annotation.meta.beanGetter
import scala.beans.BeanProperty

//import com.fasterxml.classmate.TypeResolver;
import javax.validation.Valid
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer
import org.springframework.web.context.request

import collection.JavaConversions._

;


/**
 * This controller accepts incoming request.
 * We are mapping the GET request to index() which handles the request as required.
 *
 * @author aVakil
 * @since 1.0
 */

@RestController
@RequestMapping(value = Array("/"))
@Autowired
class WalletController {

 // private val logger = LoggerFactory.getLogger(classOf[WalletController])

  val mongoCtx : ApplicationContext = new AnnotationConfigApplicationContext(classOf[MongoConfig])
  val mongoOperations : MongoOperations = mongoCtx.getBean("mongoTemplate").asInstanceOf[MongoOperations]


  @RequestMapping(value = Array("api/v1/users"), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def post( @RequestBody @Valid req: RegistrationRequest ) = {
    val currentTime = Calendar.getInstance().getTime().toString
    val createdUser: User = new User(WalletController.getUser_id, req.email, req.password, currentTime, currentTime )


     mongoOperations.save(createdUser, "user")

    val results = mongoOperations.find(new Query(Criteria where ("email") is req.email), classOf[User],"user")





    RegistrationResponse(results.get(0).userId,
      results.get(0).email,
      results.get(0).password,
      results.get(0).created_at
    )

  }

  var reqVar = ""
  var timeHash = ""
  @RequestMapping(value = Array("api/v1/users/{user_id}"), method = Array(RequestMethod.GET))
  def display(@PathVariable user_id: String, webRes:HttpServletResponse): Serializable = {

     val results = mongoOperations.find(new Query(Criteria where ("userId") is user_id), classOf[User],"user")


    if(reqVar == user_id &&  results.get(0).updated_at == timeHash ) {
      webRes.setStatus(HttpServletResponse.SC_NOT_MODIFIED)
      return "Request not modified"
    }

    if (!results.isEmpty) {
      reqVar = user_id

      timeHash = results.get(0).updated_at
      RegistrationResponse(results.get(0).userId,
       results.get(0).email,
        results.get(0).password,
        results.get(0).created_at
      )
    }
    else "No user found with user_id: " + user_id
  }



  @RequestMapping(value = Array("api/v1/users/{user_id}"), method = Array(RequestMethod.PUT))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def updateUser(@PathVariable user_id: String, @RequestBody @Valid req: RegistrationRequest) = {



    val query1 = new Query()
    query1.addCriteria(Criteria where("userId")is(user_id))
    query1.fields().include("userId")


    mongoOperations.updateFirst(query1,Update.update("email",req.email),classOf[User])
    mongoOperations.updateFirst(query1,Update.update("password", req.password),classOf[User])
    val results = mongoOperations.find(new Query(Criteria where ("userId") is user_id), classOf[User],"user")



    if (!results.isEmpty) {

      RegistrationResponse(results.get(0).userId,
        results.get(0).email,
        results.get(0).password,
        results.get(0).created_at
      )
    }
    else "No user with : " + user_id

  }

  @RequestMapping(value = Array("api/v1/users/{user_id}/idcards"), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def postCard(@RequestBody @Valid creq: CardRegistrationRequest, @PathVariable user_id: String) = {


    val createdCard: Card = new Card(WalletController.getCard_id,user_id,creq.card_name, creq.card_number,Calendar.getInstance().getTime().toString)


    mongoOperations.save(createdCard,"card")
    val results = mongoOperations.find(new Query(Criteria where ("card_name") is creq.card_name), classOf[Card],"card")

    CardRegistrationResponse(results.get(0).cardId,
      results.get(0).card_name,
      results.get(0).card_number,
      results.get(0).expirationDate
    )


  }

  @RequestMapping(value = Array("api/v1/users/{user_id}/idcards"), method = Array(RequestMethod.GET))
  @ResponseBody
  def displayCard(@PathVariable user_id: String) = {


    val results = mongoOperations.find(new Query(Criteria where ("userId") is user_id), classOf[Card],"card")

    results.foreach(println)

    var cardRes = new ListBuffer[CardRegistrationResponse]
    val resultItr = results.iterator()
    while(resultItr.hasNext) {
      val result = resultItr.next()
      cardRes += new CardRegistrationResponse(result.cardId,
        result.card_name,
        result.card_number,
        result.expirationDate)
    }

    cardRes


   }

  @RequestMapping(value = Array("api/v1/users/{user_id}/idcards/{card_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  def deleteCard(@PathVariable card_id: String, @PathVariable user_id: String) = {

    //WalletController.deleteUserCard(card_id,user_id)
    mongoOperations.remove(new Query(Criteria where ("cardId") is card_id), classOf[Card])
    //"Deleted"
  }

  @RequestMapping (value = Array("api/v1/users/{user_id}/weblogins"), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def postWeblogin(@PathVariable user_id : String, @RequestBody @Valid wreq: WebloginRegistrationRequest) = {

    val createdWeblogin : Weblogin = new Weblogin(WalletController.getWeblogin,user_id,wreq.url, wreq.login, wreq.password)

     mongoOperations.save(createdWeblogin,"weblogin")
    val results = mongoOperations.find(new Query(Criteria where ("login") is (wreq.login)), classOf[Weblogin],"weblogin")

    WebloginRegistrationResponse(results.get(0).webloginId,
      results.get(0).url,
      results.get(0).login,
      results.get(0).password
    )
  }

  @RequestMapping(value = Array("api/v1/users/{user_id}/weblogins"), method = Array(RequestMethod.GET))
  @RequestBody
  def displayWeblogin(@PathVariable user_id : String) = {

    val results=mongoOperations.find(new Query(Criteria where ("userId") is user_id), classOf[Weblogin],"weblogin")

    results.foreach(println)

    var webRes = new ListBuffer[WebloginRegistrationResponse]
    val resultItr = results.iterator()
    while(resultItr.hasNext) {
      val result = resultItr.next()
      webRes += new WebloginRegistrationResponse(result.webloginId,
        result.url,
        result.login,
        result.password)
    }

    webRes

  }

  @RequestMapping(value = Array("api/v1/users/{user_id}/weblogins/{login_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  def deleteWeblogin(@PathVariable login_id : String, @PathVariable user_id :String) ={
    mongoOperations.remove(new Query(Criteria where("webloginId") is (login_id)), classOf[Weblogin])
  }

  @RequestMapping (value = Array("api/v1/users/{user_id}/bankaccounts"), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def postBankaccounts(@PathVariable user_id : String, @RequestBody @Valid breq: BankaccountRegistrationRequest) = {

    var url = "http://www.routingnumbers.info/api/data.json?rn={rNumber}"

    var restTemplate = new RestTemplate()
    restTemplate.getMessageConverters().add(new StringHttpMessageConverter())
    var resp = restTemplate.getForObject(url,classOf[String],breq.routing_number)
    val respMap = new ObjectMapper().readValue(resp,classOf[util.HashMap[String, String]])

    if (respMap.get("code") == 200) {

      val createdBankaccount : Bankaccount = new Bankaccount(WalletController.getba_id,user_id,respMap.get("customer_name"),breq.routing_number,breq.account_number)

      mongoOperations.save(createdBankaccount, "bankaccount")

      val results = mongoOperations.find(new Query(Criteria where ("routing_number") is (breq.routing_number)), classOf[Bankaccount], "bankaccount")


        BankaccountRegistrationResponse(results.get(0).bankaccountId,
          results.get(0).account_name,
          results.get(0).routing_number,
          results.get(0).account_number)

    } else {
      "Invalid Routing Number"
    }


  }

  @RequestMapping(value = Array("api/v1/users/{user_id}/bankaccounts"), method = Array(RequestMethod.GET))
  @RequestBody
  def dislayBankaccount(@PathVariable user_id : String)={

    val results=mongoOperations.find(new Query(Criteria where ("userId") is (user_id)),classOf[Bankaccount],"bankaccount")
    results.foreach(println)


    var bankRes = new ListBuffer[BankaccountRegistrationResponse]
    val resultItr = results.iterator()
    while(resultItr.hasNext) {
      val result = resultItr.next()
      bankRes += new BankaccountRegistrationResponse(result.bankaccountId,
        result.account_name,
        result.routing_number,
        result.account_number)
    }

    bankRes


  }

  @RequestMapping(value = Array("api/v1/users/{user_id}/bankaccounts/{ba_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  def deletBankaccount (@PathVariable ba_id : String, @PathVariable user_id :String) ={
   mongoOperations.remove(new Query(Criteria where ("bankaccountId") is (ba_id)), classOf[Bankaccount])
  }

}


case class RegistrationRequest( @(NotNull@beanGetter) @BeanProperty  email: String,  @(NotNull@beanGetter) @BeanProperty  password: String)

case class RegistrationResponse(id:String,  email:String,  password:String, created_at:String)

case class EmptyResponse(msg : String)

case class CardRegistrationRequest(@(NotNull@beanGetter) @BeanProperty card_name: String,@(NotNull@beanGetter) @BeanProperty card_number: String)


case class CardRegistrationResponse(card_id:String, card_name: String, card_number: String, expirationDate: String)


case class WebloginRegistrationRequest(@(NotNull@beanGetter) @BeanProperty url : String,@(NotNull@beanGetter) @BeanProperty login : String,@(NotNull@beanGetter) @BeanProperty password : String)


case class WebloginRegistrationResponse(login_id : String,url : String, login : String, password : String)


case class BankaccountRegistrationRequest( account_name: String,@(NotNull@beanGetter) @BeanProperty routing_number: String,@(NotNull@beanGetter) @BeanProperty account_number : String)


case class BankaccountRegistrationResponse(ba_id :String,account_name: String, routing_number: String, account_number : String)



object WalletController {
  var num = 0;

  def getUser_id = {
    num = Calendar.getInstance().getTimeInMillis().toInt
    "u-" + num
  }

  var n = 0;

  def getCard_id = {
    n = Calendar.getInstance().getTimeInMillis().toInt
    "c-" + n
  }

  var w = 0;

  def getWeblogin = {
    w = Calendar.getInstance().getTimeInMillis().toInt
    "l-" + w
  }

  var b = 0

  def getba_id = {
    b = Calendar.getInstance().getTimeInMillis.toInt
    "b-" + b

  }


}
