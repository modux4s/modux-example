package mserver.example

import modux.core.api.{ModuleX, Service}
import modux.model.context.Context

case class Module(context: Context) extends ModuleX {
  override def onStart(): Unit = {
    println("onStart")
  }

  override def providers: Seq[Service] = Seq(
    UserService(context)
  )
}
