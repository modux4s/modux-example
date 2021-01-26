package services

import codecs.CodecDef
import model.Message
import modux.core.api.Service
import modux.model.ServiceDef
import modux.model.service.{Call, WebSocket}
import modux.model.ws.{OnCloseConnection, OnMessage, OnOpenConnection, WSEvent}
import org.slf4j.{Logger, LoggerFactory}

class ChatService extends Service with CodecDef {

  private lazy val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def chat: Call[WSEvent[Message, Message], Unit] = WebSocket[Message, Message] {
    case OnOpenConnection(connection) => logger.info(s"New connection ${connection.id}")
    case OnCloseConnection(connectionID) => logger.info(s"Connection $connectionID closed")
    case OnMessage(connection, message) =>
      logger.info(s"To ${message.target} to ${message.message}")
      connection.sendMessage(Message("someone", "hola!!!"))
  }

  override def serviceDef: ServiceDef = {
    namedAs("Shop chat")
      .entry(
        named("chat", chat _)
      )
  }
}
