
 package modux.core.server
 import modux.core.server.service.ModuxServer
 import scala.io.StdIn
 import modux.shared.PrintUtils

 object Main extends App{
   val server:ModuxServer = ModuxServer("modux-test", "localhost", 9000, this.getClass.getClassLoader)
   PrintUtils.info("Press enter to exit...")
   StdIn.readLine()
   PrintUtils.info("Shouting down...")
   server.stop()
 }
