package com.ihs.hotelBooking

import scala.collection.concurrent.{ TrieMap => MMap }

import java.util.Date

trait BookingManager {
  /**
   * Return true if there is no booking for the given room on the date,
   * otherwise false
   */
  def isRoomAvailable(room: Int, date: Date): Boolean
  /**
   * Add a booking for the given guest in the given room on the given
   * date. If the room is not available, throw a suitable Exception.
   */
  def addBooking(guest: String, room: Int, date: Date): Unit

  /**
   * Return a list of all the available room numbers for the given date
   */
  def getAvailableRooms(date: Date): Seq[Integer]

  /**
   * Description of a booking for a room
   */
  case class Booking(roomNumber: Int, guest: String, date: Date)

  /**
   * A map of rooms to a list of bookings for the room
   *
   * The rooms are hardcoded, for the purpose of this task
   */
  val rooms: MMap[Int, Option[List[Booking]]] = MMap(
    101 -> None,
    102 -> None,
    201 -> None,
    203 -> None
  )
}

class BookingRooms extends BookingManager {
  def isRoomAvailable(room: Int, date: Date): Boolean = {
    !rooms.getOrElse(room, None).getOrElse(List()).find(_.date == date).isDefined
  }

  /**
   * There are multiple ways to do concurrency with a mutable state, none of which
   * is great. Here I am using the current supported data structure.
   */
  def addBooking(guest: String, room: Int, date: Date): Unit = {
    if (isRoomAvailable(room, date)) {
      val booking = rooms(room) match {
        case None => Some(List(Booking(room, guest, date)))
        case x => x map {
          s: List[Booking] => {
            s.++(List(Booking(room, guest, date)))
          }
        }
      }
      rooms.put(room, booking) match {
        case None => println(s"Something went wrong when booking room $room")
        case Some(_) => println(s"room $room is booked for you.")
      }
    } else {
      throw new BookingRooms.InsertException(message = s"room $room is already booked")
    }
  }
  def getAvailableRooms(date: Date): Seq[Integer] = {
    rooms.collect {
      case x if !x._2.isDefined => x._1
      case x if (x._2.get.filter { _.date == date }.isEmpty) => x._1
    }.asInstanceOf[Seq[Integer]]
  }

}

object BookingRooms {
  class InsertException(message: String) extends Exception(message)
}

