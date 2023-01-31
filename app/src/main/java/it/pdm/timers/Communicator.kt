package it.pdm.timers

/**
 * Interfaccia che permette la comunicazione tra TimerFragment e AddTimerFragment
 */
interface Communicator {
    //interfaccia che permette la comunicazione tra TimerFragment e AddTimerFragment
    fun passData(addItem: Int)
}