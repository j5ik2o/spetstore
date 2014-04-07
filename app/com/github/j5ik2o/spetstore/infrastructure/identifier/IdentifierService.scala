package com.github.j5ik2o.spetstore.infrastructure.identifier

import java.util.concurrent.atomic.AtomicInteger

case class IdentifierService(workerId: Long = 1, datacenterId: Long = 1, var sequence: Long = 0L) {

  val ids = collection.mutable.Map.empty[Class[_], AtomicInteger]

  val twepoch = 1288834974657L
  private[this] val sequenceBits = 12L
  private[this] val workerIdBits = 5L
  private[this] val datacenterIdBits = 5L

  private[this] val workerIdShift = sequenceBits
  private[this] val datacenterIdShift = sequenceBits + workerIdBits
  private[this] val timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits

  private[this] val sequenceMask = -1L ^ (-1L << sequenceBits)

  private[this] var lastTimestamp = -1L

  private def timeGen: Long = System.currentTimeMillis()

  protected def nextId(): Long = synchronized {
    var timestamp = timeGen

    if (timestamp < lastTimestamp) {
      throw new Exception("InvalidSystemClock")
    }

    if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) & sequenceMask
      if (sequence == 0) {
        timestamp = tilNextMillis(lastTimestamp)
      }
    } else {
      sequence = 0
    }

    lastTimestamp = timestamp
    ((timestamp - twepoch) << timestampLeftShift) |
      (datacenterId << datacenterIdShift) |
      (workerId << workerIdShift) |
      sequence
  }

  protected def tilNextMillis(lastTimestamp: Long): Long = {
    var timestamp = timeGen
    while (timestamp <= lastTimestamp) {
      timestamp = timeGen
    }
    timestamp
  }

  def generate: Long = nextId()

}
