package temp

class getIEXdata {

    static void main(String[] args) {

        // get stock price from IEX
        println("Probing IEX for current stock price")

        def URL = "https://sandbox.iexapis.com/stable/stock/nkla/price?token=Tpk_1db544ada0e842f994fd2aa95674f45a".toURL()

        String data = URL.text
        println("Data is : [" + data + "]")

    }

}
