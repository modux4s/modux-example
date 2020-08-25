package modux.shop

import modux.core.api.Service
import modux.macros.serializer.SerializationSupport
import modux.model.ServiceDef
import modux.model.context.Context
import modux.model.converter.WebSocketCodec
import modux.model.service.{Call, WebSocket}
import modux.model.ws.{OnCloseConnection, OnMessage, OnOpenConnection, WSEvent}
import modux.shop.model.Message
import org.slf4j.{Logger, LoggerFactory}

case class ChatService(context: Context) extends Service with SerializationSupport {
  private lazy val logger: Logger = LoggerFactory.getLogger(this.getClass)
  implicit val messageWSCodec: WebSocketCodec[Message, Message] = codecFor[Message, Message]

  def chat: Call[WSEvent[Message, Message], Unit] = WebSocket[Message, Message] {
    case OnOpenConnection(connection) => logger.info(s"New connection ${connection.id}")
    case OnCloseConnection(connectionID) => logger.info(s"Connection $connectionID closed")
    case OnMessage(connection, message) =>
      logger.info(s"To ${message.target} to ${message.message}")
      connection.sendMessage(Message("someone", "hola!!!"))
  }

  override def serviceDef: ServiceDef = {
    namedAs("Shop chat")
      .withCalls(
        named("chat", chat _)
      )
  }
}
