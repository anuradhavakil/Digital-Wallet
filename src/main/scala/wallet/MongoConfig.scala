package wallet

import com.mongodb.{MongoClientURI, MongoClient, Mongo}
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * Created by Anu on 10/12/2014.
 */
@Configuration
 class MongoConfig extends AbstractMongoConfiguration{

  def getDatabaseName : String = "mydb"

  //def mongo:Mongo = new Mongo("localhost")
  def mongo:Mongo = new MongoClient(new MongoClientURI("mongodb://AnuradhaVakil:cmpe273@ds047040.mongolab.com:47040/mydb"))


}
