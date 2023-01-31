package it.pdm.timers

/**
 * Interfaccia che permette la comunicazione tra TimerFragment e AddTimerFragment
 */
interface Communicator {
    fun passData(addItem: Int)
}