package services

import codecs.CodecDef
import model.User
import modux.core.api.Service
import modux.model.ServiceDef
import modux.model.service.Call
import store.UserRepository

class UserService extends Service with CodecDef{

  def registerUser(): Call[User, Unit] = extractBody { user =>
    UserRepository.addUser(user)
  }

  def find(id: String): Call[Unit, User] = onCall {
    User(id, (20 + math.random() * 50).toInt)
  }

  def removeUser(name: String): Call[Unit, Unit] = onCall {
    UserRepository.removeUser(name)
  }

  override def serviceDef: ServiceDef = {

    namedAs("User service")
      .entry(
        get("/user/:id", find _),
        post("/user", registerUser _),
        delete("/user/:name", removeUser _),
      )
  }
}
