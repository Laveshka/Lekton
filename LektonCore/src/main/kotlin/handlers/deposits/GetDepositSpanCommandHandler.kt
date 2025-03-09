package handlers.deposits

import commands.deposits.DepositResult
import commands.deposits.GetDepositSpanCommand
import exceptions.DepositValidationException
import handlers.Handler
import java.math.BigDecimal
import java.math.RoundingMode

class GetDepositSpanCommandHandler : Handler<DepositResult, GetDepositSpanCommand> {
    override fun invoke(request: GetDepositSpanCommand): DepositResult {
        validateInput(request)

        var years = 0
        var currentAmount = request.amount
        val targetAmount = request.amount.multiply(request.multiplier)
        val interestRate = request.rate.divide(BigDecimal(100), 10, RoundingMode.HALF_EVEN)

        while (currentAmount < targetAmount) { // More readable than compareTo
            currentAmount = currentAmount.multiply(BigDecimal.ONE.add(interestRate))
            years++
        }

        return DepositResult(years)
    }

    private fun validateInput(request: GetDepositSpanCommand) {
        if (request.amount <= BigDecimal.ZERO) {
            throw DepositValidationException("Deposit amount must be greater than zero.")
        }
        if (request.rate <= BigDecimal.ZERO) {
            throw DepositValidationException("Interest rate must be greater than zero.")
        }
        if (request.multiplier < BigDecimal.ONE) {
            throw DepositValidationException("Multiplier must be at least 1.0.")
        }
    }
}