package modux.shop

import modux.core.api.Service
import modux.macros.serializer.codec.Codec
import modux.model.ServiceDef
import modux.model.context.Context
import modux.model.service.Call
import modux.shop.model.User
import modux.shop.store.UserRepository

case class UserService(context: Context) extends Service {

  def registerUser(): Call[User, Unit] = Call { user =>
    UserRepository.addUser(user)
  }

  def removeUser(name: String): Call[Unit, Unit] = Call.empty {
    UserRepository.removeUser(name)
  }

  override def serviceDef: ServiceDef = {
    import modux.macros.serializer.SerializationSupport._

    implicit val userCodec: Codec[User] = codecFor[User]

    namedAs("User service")
      .withCalls(
        post("/user", registerUser _),
        delete("/user/:name", removeUser _),
      )
  }
}
