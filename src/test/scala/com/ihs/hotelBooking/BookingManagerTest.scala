package com.ihs.hotelBooking

import org.scalatest._
import org.scalamock.scalatest._

import java.text.SimpleDateFormat

class BookingManagerTest extends FunSuite with BeforeAndAfter {

  var br: BookingRooms = _
  val today = new SimpleDateFormat("yyyy-MM-dd").parse("2012-03-28")
  val room = 101

  before {
    br = new BookingRooms
  }
  test("check for room availability with no reservation present") {

    assert(br.isRoomAvailable(room, today) == true)
  }

  test("check for room availability with one reservation present") {
    br.addBooking("dummy", room, today)
    assert(br.isRoomAvailable(room, today) == false)
  }

  test("can't book if booking for the date exists") {
    br.addBooking("dummy", room, today)
    assertThrows[BookingRooms.InsertException](br.addBooking("dummy", room, today))
  }
}
