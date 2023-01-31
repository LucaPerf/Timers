package it.pdm.timers

/**
 * Interfaccia che permette la comunicazione tra la classe TimerFragment e la classe AddTimerFragment
 */
interface Communicator {
    //interfaccia che permette la comunicazione tra TimerFragment e AddTimerFragment
    fun passData(addItem: Int)
}