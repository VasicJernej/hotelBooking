import java.text.SimpleDateFormat

import com.ihs.hotelBooking.BookingRooms

object Service extends App {

  val br = new BookingRooms
  val today = new SimpleDateFormat("yyyy-MM-dd").parse("2012-03-28")
  println(br.isRoomAvailable(101, today)) // outputs true
  br.addBooking("Smith", 101, today)
  println(br.isRoomAvailable(101, today)) // outputs false
  try {
    br.addBooking("Jones", 101, today) // throws an exception
  } catch {
    case e: BookingRooms.InsertException => println(e.getMessage)
  }
}
