package application

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.web.bind.annotation.*

private const val SPACE = " "

@RestController
class WordController {

    @RequestMapping(path = ["api/count"], method = [RequestMethod.POST])
    fun countWord(@RequestBody requestBody: RequestMessage): ResponseMessage {
        val responseMessage = ResponseMessage(message = requestBody.message)
        val clearedMessage = requestBody.message.replace("\\s+".toRegex(), " ").trim()
        responseMessage.resMessage = if (clearedMessage == SPACE || clearedMessage.isEmpty()) "0" else clearedMessage.split(SPACE).size.toString()
        return  responseMessage
    }

    @RequestMapping(path = ["api/space/split"], method = [RequestMethod.POST])
    fun formatSplitter(@RequestBody requestBody: RequestMessage,@RequestParam separator: String): ResponseMessage {
        val responseMessage = ResponseMessage(message = requestBody.message)
        val clearedMessage = requestBody.message.replace("\\s+".toRegex(), " ").trim()
        responseMessage.resMessage = when (separator.toUpperCase()) {
            "COMMA" -> clearedMessage.split(SPACE).joinToString(separator = ",") { it }
            "SEMICOLON" -> clearedMessage.split(SPACE).joinToString(separator = ";") { it }
            "COLON" -> clearedMessage.split(SPACE).joinToString(separator = ":") { it }
            "SLASH" -> clearedMessage.split(SPACE).joinToString(separator = "/") { it }
            else -> clearedMessage
        }

        return  responseMessage
    }
}

class ResponseMessage(val message: String,var resMessage : String? = "Default Message")


data class RequestMessage @JsonCreator constructor(
    val message: String
)
