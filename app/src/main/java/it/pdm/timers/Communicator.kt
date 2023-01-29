package it.pdm.timers

interface Communicator {
    //interfaccia che permette la comunicazione tra TimerFragment e AddTimerFragment
    fun passData(addItem: Int)
}