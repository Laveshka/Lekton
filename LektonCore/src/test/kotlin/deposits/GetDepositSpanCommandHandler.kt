package deposits

import commands.deposits.GetDepositSpanCommand
import exceptions.DepositValidationException
import handlers.deposits.GetDepositSpanCommandHandler
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

class GetDepositSpanCommandHandlerTest {
    private val handler = GetDepositSpanCommandHandler()

    @Test
    fun `test valid case`() {
        val command = GetDepositSpanCommand(
            amount = BigDecimal(300),
            rate = BigDecimal(10),
            multiplier = BigDecimal(2)
        )
        val result = handler.invoke(command)
        assertEquals(8, result.years)
    }

    @Test
    fun `test zero deposit amount`() {
        val command = GetDepositSpanCommand(
            amount = BigDecimal.ZERO,
            rate = BigDecimal(10),
            multiplier = BigDecimal(2)
        )
        assertThrows<DepositValidationException> { handler.invoke(command) }
    }

    @Test
    fun `test negative interest rate`() {
        val command = GetDepositSpanCommand(
            amount = BigDecimal(300),
            rate = BigDecimal(-5),
            multiplier = BigDecimal(2)
        )
        assertThrows<DepositValidationException> { handler.invoke(command) }
    }

    @Test
    fun `test multiplier less than one`() {
        val command = GetDepositSpanCommand(
            amount = BigDecimal(300),
            rate = BigDecimal(10),
            multiplier = BigDecimal(0.5)
        )
        assertThrows<DepositValidationException> { handler.invoke(command) }
    }
}
