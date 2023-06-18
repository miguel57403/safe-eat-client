package mb.safeEat.services.api.models

import mb.safeEat.components.Order
import java.time.LocalDateTime


data class Notification (
  val id: String? = null,
  val content: String? = null,
  val time: LocalDateTime? = null,
  val isViewed:Boolean? = false,
  val order: Order? = null
)