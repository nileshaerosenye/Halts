package temp

import java.text.SimpleDateFormat

class DateConvert {

    static void main(String[] args) {

        Date now = new Date()
        TimeZone tz = TimeZone.getTimeZone("EST")
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        SimpleDateFormat HHformat = new SimpleDateFormat("HH")

        println("Now is : " + now)
        if (now.after(HHformat.parse("08")) && now.before(HHformat.parse("24"))) {
            println("We are in trading window")
        }
        else {
            println("Outside the trading window")
        }

    }

}
