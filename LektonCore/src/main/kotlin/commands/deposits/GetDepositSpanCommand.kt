package commands.deposits

import commands.Command
import java.math.BigDecimal

data class GetDepositSpanCommand(
    val amount: BigDecimal,
    val rate: BigDecimal,
    val multiplier: BigDecimal) : Command<DepositResult>