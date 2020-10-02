package temp

import com.opencsv.CSVReader
import restApi.StockHalted

import javax.swing.JOptionPane

class CSVprocessing {

    static void main(String[] args) {

        def url = "https://www.nyse.com/api/trade-halts/current/download".toURL()
        int LineCount=0
        Reader reader = url.newReader()
        CSVReader csvReader = new CSVReader( reader )

        String[] nextRecord
        while ((nextRecord = csvReader.readNext()) != null) {
            if ( LineCount > 0 && nextRecord.length>1 ) {

                String haltDate = nextRecord[0]
                String haltTime = nextRecord[1]
                String Symbol   = nextRecord[2]
                String Name     = nextRecord[3]
                String Exchange = nextRecord[4]
                String Reason   = nextRecord[5]
                String resumeDate=nextRecord[6]
                String resumeTime=nextRecord[7]

                println("Sym: " + Symbol + " Halt: " + haltDate+"-"+haltTime + " Resume: " + resumeDate+"-"+resumeTime)


            }
            LineCount++
        }


    }

}
