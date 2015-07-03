
package wallet
/**
 * Created by Anu on 9/19/2014.
 */



import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
;

import org.springframework.context.annotation.{AnnotationConfigApplicationContext, ComponentScan}
import org.springframework.data.mongodb.core.MongoOperations
;
/**
 * This object bootstraps Spring Boot web application.
 * Via Gradle: gradle bootRun
 *
 * @author saung
 * @since 1.0
 */


object WalletWebApplication {

  def main(args: Array[String]){


    SpringApplication.run(classOf[WalletConfig]);
  }
}



