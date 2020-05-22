package machine

import java.util.*

fun main() {
    println("")
    val scanner = Scanner(System.`in`)
    val coffeeMachine = CoffeeMachine()
    while (coffeeMachine.state != CoffeeMachine.State.EXIT) {
        if (coffeeMachine.state == CoffeeMachine.State.READY) {
            print("Write action (buy, fill, take, remaining, exit): > ")
        }
        val command = scanner.next()
        coffeeMachine.work(command)
    }
}

class CoffeeMachine(var water: Int = 400, var milk: Int = 540, var coffee: Int = 120, var cups: Int = 9,
                    var cash: Int = 550) {
    var state: State = State.READY

    fun work(userInput: String) {
        when {
            state == State.READY && userInput == "remaining" -> processRemaining()
            state == State.READY && userInput == "buy" -> showSelection()
            state == State.TAKE_SELECTION -> processSelection(userInput)
            state == State.READY && userInput == "fill" -> prepareFilling()
            state == State.FILL_WATER -> filling(userInput, state)
            state == State.FILL_MILK -> filling(userInput, state)
            state == State.FILL_COFFEE -> filling(userInput, state)
            state == State.FILL_CUPS -> filling(userInput, state)
            state == State.READY && userInput == "take" -> withdraw()
            state == State.READY && userInput == "exit" -> exit()
        }
    }

    private fun exit() {
        this.state = State.EXIT
    }

    private fun withdraw() {
        println("I gave you $cash")
        this.cash = 0
    }

    private fun filling(input: String, state: State) {
        val fill = input.toInt()
        when (state) {
            State.FILL_WATER -> {
                this.water += fill
                this.state = State.FILL_MILK
                print("Write how many ml of milk do you want to add: > ")
            }
            State.FILL_MILK -> {
                this.milk += fill
                this.state = State.FILL_COFFEE
                print("Write how many grams of coffee beans do you want to add: > ")
            }
            State.FILL_COFFEE -> {
                this.coffee += fill
                this.state = State.FILL_CUPS
                print("Write how many disposable cups of coffee do you want to add: > ")
            }
            State.FILL_CUPS -> {
                this.cups += fill
                this.state = State.READY
            }
        }
    }

    private fun prepareFilling() {
        state = State.FILL_WATER
        println()
        print("Write how many ml of water do you want to add: > ")
    }

    private fun processSelection(select: String) {
        when (select) {
            "1" -> brewCoffee(Ingredients.ESPRESSO)
            "2" -> brewCoffee(Ingredients.LATTE)
            "3" -> brewCoffee(Ingredients.CAPPUCINO)
            else -> {}
        }
        state = State.READY
    }

    private fun brewCoffee(selection: Ingredients) {
        if (water < selection.water) {
            println("Sorry, not enough water!")
        } else if (milk < selection.milk) {
            println("Sorry, not enough milk!")
        } else if (coffee < selection.coffee) {
            println("Sorry, not enough coffee!")
        } else if (cups == 0) {
            println("Sorry, not enough cups!")
        } else {
            println("I have enough resources, making you a coffee!")
            water -= selection.water
            milk -= selection.milk
            coffee -= selection.coffee
            cups--
            cash += selection.cash
        }
        state = State.READY
    }

    private fun showSelection() {
        println()
        print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: > ")
        state = State.TAKE_SELECTION
    }

    private fun processRemaining() {
        println()
        println("The coffee machine has:")
        println("$water of water")
        println("$milk of milk")
        println("$coffee of coffee beans")
        println("$cups of disposable cups")
        println("$${cash} of money")
        println()
    }

    enum class State {
        READY, TAKE_SELECTION, FILL_WATER, FILL_MILK, FILL_COFFEE, FILL_CUPS, EXIT
    }

    enum class Ingredients(val water: Int, val milk: Int, val coffee: Int, val cash: Int) {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCINO(200, 100, 12, 6)
    }
}

