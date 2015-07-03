package wallet

/**
 * Created by Anu on 9/19/2014.
 */



import org.springframework.context.annotation.{Bean, Primary, Configuration, ComponentScan}
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * This config class will trigger Spring @annotation scanning and auto configure Spring context.
 *
 * @author saung
 * @since 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
class WalletConfig{

  @Bean
  @Primary
  def scalaObjectMapper() = { new ScalaObjectMapper }

}

