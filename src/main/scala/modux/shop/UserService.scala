package modux.shop

import modux.core.api.Service
import modux.macros.serializer.codec.Codec
import modux.model.ServiceDef
import modux.model.context.Context
import modux.model.service.Call
import modux.shop.model.User
import modux.shop.store.UserRepository

case class UserService(context: Context) extends Service {

  def registerUser(): Call[User, Unit] = Call { (user: User) =>
    UserRepository.addUser(user)
  }.mapResponse(resp => resp.withHeader("test", "test"))

  def find(id: String): Call[Unit, User] = Call.handleRequest { request =>
    if (request.hasHeader("sessionID")) {
      User(id, (20 + math.random() * 50).toInt)
    } else {
      Unauthorized("You must be logged")
    }
  }

  def removeUser(name: String): Call[Unit, Unit] = Call.empty {
    UserRepository.removeUser(name)
  }

  override def serviceDef: ServiceDef = {
    import modux.macros.serializer.SerializationSupport._

    implicit val userCodec: Codec[User] = codecFor[User]

    namedAs("User service")
      .withCalls(
        get("/user/:id", find _),
        post("/user", registerUser _),
        delete("/user/:name", removeUser _),
      )
  }
}
