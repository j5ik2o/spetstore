package spetstore.interface.util

import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel

/**
  * This code is originated from Spray.
  * https://github.com/spray/spray/blob/b473d9e8ce503bafc72825914f46ae6be1588ce7/spray-util/src/main/scala/spray/util/Utils.scala#L35-L47
  */
trait RandomPortSupport {

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
