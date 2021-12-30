package application.pojo

import java.math.BigDecimal


class Payment {
    var amount: BigDecimal? = null
    var cardNumber: String? = null
    var cvv: String? = null
    var month: String? = null
    var year: String? = null
}