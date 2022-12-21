package it.pdm.timers

interface Communicator {
    //interfaccia che permette la comunicazione tra TimerFragment e TimerFragmentSalvati
    fun passData(addItem : String)
}