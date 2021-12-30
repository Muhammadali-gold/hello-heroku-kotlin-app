package application.pdfcontroller


import application.pojo.*
import java.math.BigDecimal


object OrderHelper {
    val order: Order
        get() {
            val order = Order()
            order.orderId=1234
            order.date="2077-09-05"
            val address = Address()
            address.city="New York"
            address.street="2897  Geneva Street"
            address.zipCode="10016"
            address.state="New York"
            val account = Account()
            account.phoneNumber="917-483-5146"
            account.email="628jh4h624y@temporary-mail.net"
            account.name="Mr. Eugene A King"
            account.address=address
            order.account=account
            val items: MutableList<Item> = ArrayList()
            order.items=items
            val item1 = Item()
            item1.name="Blue T-Shirt"
            item1.price=BigDecimal.valueOf(9.99)
            item1.quantity=2
            item1.sku="100034"
            items.add(item1)
            val item2 = Item()
            item2.name="Green SweatShirt"
            item2.price=BigDecimal.valueOf(12.49)
            item2.sku="100075"
            item2.quantity=3
            items.add(item2)
            val item3 = Item()
            item3.name="Grey Sports Shoes"
            item3.price=BigDecimal.valueOf(14.49)
            item3.sku="100022"
            item3.quantity=1
            items.add(item3)
            val payment = Payment()
            val totalPrice2=BigDecimal.ZERO;
            for (item in items) {
                totalPrice2.add(item.price?.multiply(item.quantity.toString().toBigDecimal()) ?: BigDecimal.ZERO)
            }
//            val totalPrice: BigDecimal = items.stream().map<Any> { item: Item ->
//                item.price?.multiply(BigDecimal(item.quantity.toString())) ?: BigDecimal.ZERO
//            }.reduce(BigDecimal.ZERO, BigDecimal::add) as BigDecimal
            payment.amount=totalPrice2
            payment.cardNumber="4111111111111111"
            payment.cvv="123"
            payment.month="04"
            payment.year="2030"
            order.payment=payment
            return order
        }
}
//object OrderHelper {
//    val order: Order
//        get() {
//            val order = Order()
//            order.setOrderId(1234)
//            order.setDate("2077-09-05")
//            val address = Address()
//            address.setCity("New York")
//            address.setStreet("2897  Geneva Street")
//            address.setZipCode("10016")
//            address.setState("New York")
//            val account = Account()
//            account.setPhoneNumber("917-483-5146")
//            account.setEmail("628jh4h624y@temporary-mail.net")
//            account.setName("Mr. Eugene A King")
//            account.setAddress(address)
//            order.setAccount(account)
//            val items: MutableList<Item> = ArrayList()
//            order.setItems(items)
//            val item1 = Item()
//            item1.setName("Blue T-Shirt")
//            item1.setPrice(BigDecimal.valueOf(9.99))
//            item1.setQuantity(2)
//            item1.setSku("100034")
//            items.add(item1)
//            val item2 = Item()
//            item2.setName("Green SweatShirt")
//            item2.setPrice(BigDecimal.valueOf(12.49))
//            item2.setSku("100075")
//            item2.setQuantity(3)
//            items.add(item2)
//            val item3 = Item()
//            item3.setName("Grey Sports Shoes")
//            item3.setPrice(BigDecimal.valueOf(14.49))
//            item3.setSku("100022")
//            item3.setQuantity(1)
//            items.add(item3)
//            val payment = Payment()
//            val totalPrice: BigDecimal = items.stream().map<Any> { item: Item ->
//                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
//            }.reduce(BigDecimal.ZERO, BigDecimal::add)
//            payment.setAmount(totalPrice)
//            payment.setCardNumber("4111111111111111")
//            payment.setCvv("123")
//            payment.setMonth("04")
//            payment.setYear("2030")
//            order.setPayment(payment)
//            return order
//        }
//}