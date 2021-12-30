package application.chunk

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class UserController {
    @RequestMapping("/user", method = [RequestMethod.GET])
    fun getUser() : User {
        val user = User(
            username = "grahamcox",
            screenName = "Graham",
            email = "grahamcox@example.com",
            registered = Instant.now()
        )
        return user
    }


    /**
     * Pretend to create a new user
     * @param user The details of the user to create
     */
    @RequestMapping(value = ["/user"], method = [RequestMethod.POST])
    fun createUser(@RequestBody user: NewUser): User {
        return User(
            username = user.username,
            screenName = user.screenName,
            email = user.email,
            registered = Instant.now()
        )
    }

    /** Cause an error to occur */
    @RequestMapping("/raiseError")
    fun raiseError() {
        throw IllegalArgumentException("This shouldn't have happened")
    }

    /** Handle the error */
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleError(e: IllegalArgumentException) = e.message
}