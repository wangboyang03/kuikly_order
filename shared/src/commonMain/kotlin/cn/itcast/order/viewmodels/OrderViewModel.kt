package cn.itcast.order.viewmodels

import cn.itcast.order.models.OrderViewState
import com.tencent.kuikly.core.base.PagerScope
import com.tencent.kuikly.core.reactive.handler.observable

class OrderViewModel(scope: PagerScope) {
  private var _state: OrderViewState by scope.observable(OrderViewState())
  val state: OrderViewState get() = _state

  fun selectCategory(category: String): Boolean {
    if (category !in state.categories || category == state.selectedCategory) return false
    _state = state.copy(selectedCategory = category)
    return true
  }

  fun addDish(): Boolean {
    if (!state.dish.inStock) return false
    _state = state.copy(dishCount = state.dishCount + 1)
    return true
  }

  fun decreaseDish(): Boolean {
    if (state.dishCount == 0) return false
    _state = state.copy(dishCount = (state.dishCount - 1).coerceAtLeast(0))
    return true
  }

  fun openCart(): Boolean {
    if (state.dishCount == 0) return false
    return selectCategory(state.dish.category)
  }

  fun checkout(): Boolean = state.canCheckout
}
