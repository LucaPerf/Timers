package it.pdm.timers

//data class Timer(val NTimer: String, val timeTimer: String)

class Timer{
    var minuti: String? = null
    var secondi: String? = null

    constructor(minuti: String?, secondi: String?){
        this.minuti = minuti
        this.secondi = secondi
    }
    constructor(){

    }
}
