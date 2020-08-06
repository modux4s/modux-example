package mserver.example

import java.nio.file.{Path, Paths}
import java.time.ZonedDateTime

import akka.NotUsed
import akka.io.IO
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging
import modux.core.api.Service
import modux.macros.serializer.SerializationSupport
import modux.macros.serializer.SerializationSupport.DefaultCodecRegistry
import modux.macros.serializer.codec.Codec
import modux.model.ServiceDescriptor
import modux.model.context.Context
import modux.model.converter.WebSocketCodec
import modux.model.service.{Call, WebSocket}
import modux.model.ws.{OnCloseConnection, OnMessage, OnOpenConnection, WSEvent}

case class UserService(context: Context) extends Service with SerializationSupport with LazyLogging {

  implicit val userCodec: Codec[User] = codify[User]
  implicit val wsCodes: WebSocketCodec[String, User] = websocketCodec[String, User]

  def addUser(): Call[User, Unit] = { user =>
    logger.info(s"user $user created")
  }

  def getUser(id: String): Call[Unit, User] = Call {
    if (math.random() < 0.5) {
      NotFound(s"User $id not found")
    } else
      User(id, ZonedDateTime.now().minusYears(10))
  }

  def getUsers(age: Int): Call[Unit, Source[User, NotUsed]] = Call {
    Source(List[User](User("Frank", ZonedDateTime.now().minusYears(age))))
  }

  def uploadFile(name: String, ext: String): Call[Source[ByteString, Any], Unit] = { src =>
    val file: Path = Paths.get(s"$name.$ext")
    val _ = src.runWith(FileIO.toPath(file))
  }

  def ws(): Call[WSEvent[String, User], Unit] = WebSocket[String, User] {
    case OnOpenConnection(connection) => logger.info(s"Connection $connection created")
    case OnCloseConnection(connection) => logger.info(s"Connection $connection closed")
    case OnMessage(connection, message) => connection.sendMessage(User(message, ZonedDateTime.now()))
  }

  override def serviceDescriptor: ServiceDescriptor =
    namedAs("user-service")
      .withCalls(
        post("/user", addUser _),
        get("/user/{id}", getUser _),
        get("/user?age", getUsers _),
        post("/upload/{name}/{ext}", uploadFile _),
        named("ws", ws _)
      )
}
