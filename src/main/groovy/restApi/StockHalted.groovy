package restApi

import java.text.SimpleDateFormat

class StockHalted {

    String Symbol
    String CompanyName
    String Reason
    Date HaltTMS
    Date ResumeTMS
    boolean isResumed = false
    float HaltedPrice
    float ResumedPrice

    public StockHalted( String Symbol, String CompanyName, String HaltDate, String HaltTime, String Reason, String ResumeDate, String ResumeTime ) {

        this.Symbol = Symbol
        this.CompanyName = CompanyName
        this.Reason = Reason

        String formatIN = "MM/dd/yyyy HH:mm:ss"
        HaltTMS = new SimpleDateFormat( formatIN ).parse( "${HaltDate} ${HaltTime}" )

        if ( ! ResumeTime.isEmpty() ) {
            ResumeTMS = new SimpleDateFormat(formatIN).parse("${ResumeDate} ${ResumeTime}")
            this.isResumed = true
        }

        // if it is halted, then get its halt price at this moment
//        println("Getting HaltedPrice")

    }


    @Override
    public String toString() {
        return "Sym: " + Symbol + "\tHaltTMS: " + HaltTMS + "\tReason: " + Reason + "\tResumeTMS: " + ResumeTMS
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        StockHalted that = (StockHalted) o

        if (HaltTMS != that.HaltTMS) return false
//        if (ResumeTMS != that.ResumeTMS) return false
        if (Symbol != that.Symbol) return false

        return true
    }

    int hashCode() {
        int result
        result = Symbol.hashCode()
        result = 31 * result + HaltTMS.hashCode()
//        result = 31 * result + (ResumeTMS != null ? ResumeTMS.hashCode() : 0)
        return result
    }
}
