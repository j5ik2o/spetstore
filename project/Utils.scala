import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel

import sbt._

object Utils {

  implicit class SbtLoggerOps(val self: sbt.Logger) extends AnyVal {

    def toScalaProcessLogger: scala.sys.process.ProcessLogger = new scala.sys.process.ProcessLogger {
      private val _log                     = new FullLogger(self)
      override def out(s: => String): Unit = _log.info(s)

      override def err(s: => String): Unit = _log.err(s)

      override def buffer[T](f: => T): T = _log.buffer(f)
    }
  }

  object RandomPortSupport {

    def temporaryServerAddress(interface: String = "127.0.0.1"): InetSocketAddress = {
      val serverSocket = ServerSocketChannel.open()
      try {
        serverSocket.socket.bind(new InetSocketAddress(interface, 0))
        val port = serverSocket.socket.getLocalPort
        new InetSocketAddress(interface, port)
      } finally serverSocket.close()
    }

    def temporaryServerHostnameAndPort(interface: String = "127.0.0.1"): (String, Int) = {
      val socketAddress = temporaryServerAddress(interface)
      socketAddress.getHostName -> socketAddress.getPort
    }

    def temporaryServerPort(interface: String = "127.0.0.1"): Int =
      temporaryServerHostnameAndPort(interface)._2
  }

}
