package temp

import java.text.SimpleDateFormat

class DateConvert {

    static void main(String[] args) {

        println("Date concatenation")
        // TMS=09/28/2020 06:55:37

        Date now = new Date()
        TimeZone tz = TimeZone.getTimeZone("GMT-4:00")
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

        // Is this the trading hour window ?
        String s_SOD = now.format(format).toString().split("T")[0] + "T09:30:00.000-0400"
        String s_EOD = now.format(format).toString().split("T")[0] + "T23:59:00.000-0400"
        Date SOD = new SimpleDateFormat( format ).parse( s_SOD )
        Date EOD = new SimpleDateFormat( format ).parse( s_EOD )

        String formatIN = "MM/dd/yyyy HH:mm:ss"
        String TMS="09/28/2020 09:30:34"
        Date HaltTMS = new SimpleDateFormat( formatIN ).parse( TMS )
        println("Halt date : " + HaltTMS)


    }

}
