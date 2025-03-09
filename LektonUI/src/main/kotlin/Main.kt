package org.itmo

import handlers.deposits.GetDepositSpanCommandHandler
import org.itmo.console.DepositCalculator

fun main() {
    val handler = GetDepositSpanCommandHandler()
    val calculator = DepositCalculator(handler)
    calculator.start()
}