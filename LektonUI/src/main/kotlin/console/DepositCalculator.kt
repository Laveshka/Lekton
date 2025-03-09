package org.itmo.console

import commands.deposits.GetDepositSpanCommand
import handlers.deposits.GetDepositSpanCommandHandler
import java.util.*

class DepositCalculator(private val handler: GetDepositSpanCommandHandler) {
    private val scanner = Scanner(System.`in`)

    fun start() {
        println("Welcome to the Deposit Growth Calculator!")
        println("Type 'calculate-deposit' followed by options to compute.")
        println("Type '-q' or '--quit' to exit.")

        while (true) {
            val input = scanner.nextLine().trim()
            val args = input.split(" ").toTypedArray()

            if (args.isEmpty()) continue

            when (args[0]) {
                "calculate-deposit" -> processCalculation(args.drop(1).toTypedArray())
                "-q", "--quit" -> {
                    println("Exiting program. Goodbye!")
                    return
                }
                "--help", "-h" -> printHelp()
                else -> println("Unknown command: ${args[0]}. Type '--help' for usage.")
            }
        }
    }

    private fun processCalculation(args: Array<String>) {
        val parsedArgs = parseArgs(args)

        if (parsedArgs.containsKey("help")) {
            printHelp()
            return
        }

        try {
            val amount = parsedArgs["amount"]?.toBigDecimal() ?: throw IllegalArgumentException("Missing --amount/-a flag")
            val rate = parsedArgs["rate"]?.toBigDecimal() ?: throw IllegalArgumentException("Missing --rate/-r flag")
            val multiplier = parsedArgs["multiplier"]?.toBigDecimal() ?: throw IllegalArgumentException("Missing --multiplier/-m flag")

            val command = GetDepositSpanCommand(amount, rate, multiplier)
            val result = handler.invoke(command)

            println("It will take approximately ${result.years} years for your deposit to reach the target amount.")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun parseArgs(args: Array<String>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        var currentKey: String? = null

        for (arg in args) {
            if (arg.startsWith("--") || arg.startsWith("-")) {
                currentKey = flagAliases[arg] ?: arg.removePrefix("--")

                if (currentKey == "help") {
                    map[currentKey] = "true"
                    return map
                }
            } else if (currentKey != null) {
                map[currentKey] = arg
                currentKey = null
            }
        }

        return map
    }

    private fun printHelp() {
        println("""
        Usage:
        calculate-deposit -a <amount> -r <rate> -m <multiplier>
        
        Commands:
        calculate-deposit   Compute deposit growth based on input values.
        -q, --quit          Exit the program.
        --help, -h          Show this help message.
        
        Flags:
        -a, --amount        Initial deposit amount (must be positive).
        -r, --rate          Annual interest rate in percentage (must be positive).
        -m, --multiplier    Target multiplier for growth (e.g., 2 for doubling).

        Example:
        calculate-deposit -a 300 -r 10 -m 2
        """.trimIndent())
    }

    private val flagAliases = mapOf(
        "-a" to "amount",
        "-r" to "rate",
        "-m" to "multiplier",
        "-h" to "help"
    )
}
