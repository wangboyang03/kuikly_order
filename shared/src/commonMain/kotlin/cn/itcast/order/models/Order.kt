package cn.itcast.order.models

data class OrderDish(
  val id: Int,
  val name: String,
  val category: String,
  val price: Int,
  val cover: String,
  val description: String,
  val tags: List<String>,
  val spicy: String,
  val inStock: Boolean
)

data class OrderViewState(
  val categories: List<String> = listOf("推荐", "休闲", "美食", "亲密"),
  val dish: OrderDish = OrderDish(
    id = 1,
    name = "要抱抱",
    category = "亲密",
    price = 0,
    cover = "抱抱",
    description = "抱抱贴贴蹭蹭",
    tags = listOf("推荐", "情侣必点"),
    spicy = "承诺一定要做到呦",
    inStock = true
  ),
  val selectedCategory: String = "推荐",
  val dishCount: Int = 0
) {
  val dishVisible: Boolean
    get() = selectedCategory == "推荐" || selectedCategory == dish.category

  val totalPrice: Int
    get() = dish.price * dishCount

  val canCheckout: Boolean
    get() = dishCount > 0
}
