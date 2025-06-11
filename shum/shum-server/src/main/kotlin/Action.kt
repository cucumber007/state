import model.Message

sealed interface Action {
    data class NewMessage(val message: Message) : Action
//    data class Error(val message: String) : Action
}