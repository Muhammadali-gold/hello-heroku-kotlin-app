package application.pojo

class Order {
    var orderId: Int? = null
    var date: String? = null
    var account: Account? = null
    var payment: Payment? = null
    var items: List<Item>? = null
}