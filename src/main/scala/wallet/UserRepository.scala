package wallet

import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by Anu on 10/12/2014.
 */
trait UserRepository extends MongoRepository[User, String]
