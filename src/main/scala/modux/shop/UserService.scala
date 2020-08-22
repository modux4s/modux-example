package modux.shop

import modux.core.api.Service
import modux.model.ServiceDef
import modux.model.context.Context

case class UserService(context:Context) extends Service {
  override def serviceDef: ServiceDef ={
    namedAs("user-service")
      .withCalls()
  }
}
