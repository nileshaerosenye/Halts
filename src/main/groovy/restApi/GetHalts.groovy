package restApi

import com.opencsv.CSVReader

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import javax.swing.JDialog
import javax.swing.JOptionPane
import java.text.SimpleDateFormat

class GetHalts {

    static ArrayList<StockHalted> HaltedStocks = new ArrayList<StockHalted>()
    static boolean isFirstLoop = true
    static long sleepMilliSec = 30000

    public static void main(String[] args) {

        // Run this in a loop every 30 seconds
        Date now

        // run a loop for 10 instances
        while ( true )
        {
            // are we in trading window ?
            now = new Date()
            int Hrs = Integer.parseInt( now.format("HH") )

            if ( Hrs > 6 && Hrs < 20 ) {
//                println("We are in trading window")
                // get the csv containing halted stocks
                getHaltsInCSV()

                // Display the halted stocks
                int LineCount = 0
                println("Time : " + now + "\t\t [ Halts : " + HaltedStocks.size() + " ] \t\t [ Sleep : " + sleepMilliSec/1000 + " sec ] ")
                HaltedStocks.each { haltedStock ->
                    println(LineCount++ + " : " + haltedStock)
                }
                println("------------------------------------------------------------------------------------------------------------------------\n")

            }
            else {
                println("Trading closed: outside of trading hours. Exiting.")
                System.exit(0)
            }

            // do not show the alert on first loop of the run.
            if ( isFirstLoop ) {
                isFirstLoop = false
            }

            // sleep for 30 seconds before getting the data again
            sleep( sleepMilliSec )
        }

    }

    private static ArrayList<StockHalted> getHaltsInCSV() {

        def url = "https://www.nyse.com/api/trade-halts/current/download".toURL()
//        File url = new File("/Users/nileshmune/workspace/data/halts.csv")

        ArrayList<StockHalted> removeList = new ArrayList<StockHalted>()

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
//                println("Sym: " + Symbol + " Halt: " + haltDate+"-"+haltTime + " Resume: " + resumeDate+"-"+resumeTime)

                // check if the stock is already in the list, then add
                StockHalted halt = new StockHalted( Symbol, Name, haltDate, haltTime, Reason, resumeDate, resumeTime )
                boolean isAlreadyInList = false
                boolean isResumed = false

                // check in the arrayList
                HaltedStocks.each {

                    // Check if the Symbol and HaltTMS is same, then check that ResumeDate is not same, then remove it and add the new one
                    if ( it.Symbol == halt.Symbol && it.HaltTMS == halt.HaltTMS ) {
                        // if previously halted and now resumed
                        if ( ! it.ResumeTMS ) {
                            // now check if halt stock has a new resumeTMS
                            if ( halt.ResumeTMS ) {
//                                println("resume TMS was null. Found new ResumeTMS")
                                removeList.add( it )
                                isResumed = true
                            }
                        }
                        isAlreadyInList = true
                    }
                }

                if ( ! isAlreadyInList ) {
                    HaltedStocks.add( halt )
                    playAudio( "sweetalertsound.wav" )
                    if ( ! isFirstLoop ) {
                        println("Halt ALERT | " + halt)
                        showDialog( halt )
                    }
                }
                if ( isResumed ) {
//                    println("Resumed. Adding to the list")
                    println("Resume ALERT | " + halt)
                    HaltedStocks.add( halt )
                    playAudio( "correct.wav" )
                    // show dialog
                    showDialog( halt )
                }

            }
            LineCount++
        }

        // these positions are to be deleted
        removeList.each {
            HaltedStocks.remove( it )
        } as ArrayList<StockHalted>



    }

    private static void playAudio( String fName ) {
//        println("Playing audio alert")

        InputStream is = GetHalts.class.getResourceAsStream("/sounds/${fName}")
        InputStream bufferedIn = new BufferedInputStream(is);

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn)

        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();

    }

    private static void showDialog ( StockHalted halt ) {

        JOptionPane op
        final JDialog dialog

        if ( halt.ResumeTMS ) {
            op = new JOptionPane("Resumed : ${halt.Symbol}", JOptionPane.INFORMATION_MESSAGE)
            dialog = op.createDialog("Stock Resume Alert")
            dialog.setLocation(500,10)
        }
        else {
            op = new JOptionPane("Halted : ${halt.Symbol}", JOptionPane.INFORMATION_MESSAGE)
            dialog = op.createDialog("Stock Halt Alert")
            dialog.setLocation(700,10)
        }

        // Create a new timer
        Timer timer = new Timer();

        // Perform this task after some seconds
        timer.schedule(new TimerTask() {
            public void run() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }, 10000);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.setVisible(true);

    }

}