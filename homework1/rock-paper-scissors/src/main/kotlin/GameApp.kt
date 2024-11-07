package org.example

import java.security.SecureRandom

fun main() {
    println("Приветствуем Вас в игре 'Камень, ножницы, бумага, ящерица, спок'! \n" +
            "Пожалуйста, ознакомьтесь с правилами.\n")
    println("Вам предлагается выбрать один из пяти инструментов: камень, ножницы, бумагу, ящерицу или спок.\n" +
            "Вот краткое описание того, как они действуют друг на друга:\n")

    println("При выборе камня:\n" +
            "Камень проиграет бумаге: бумага заворачивает камень.\n" +
            "Камень одолеет ножницы: камень разбивает ножницы.\n" +
            "Камень одолеет ящерицу: камень давит ящерицу.\n" +
            "Камень проиграет споку: спок испаряет камень.\n")

    println("При выборе ножниц:\n" +
            "Ножницы проиграют камню: камень разбивает ножницы.\n" +
            "Ножницы одолеет бумагу: ножницы режут бумагу.\n" +
            "Ножницы одолеет ящерицу: ножницы отрезают голову ящерице.\n" +
            "Ножницы проиграет споку: спок ломает ножницы.\n")

    println("При выборе бумаги:\n" +
            "Бумага одолеет камень: бумага заворачивает камень.\n" +
            "Бумага проиграет ножницам: ножницы режут бумагу.\n" +
            "Бумага проиграет ящерице: ящерица ест бумагу.\n" +
            "Бумага одолеет спока: бумага подставляет спока.\n")

    println("При выборе ящерицы:\n" +
            "Ящерица проиграет камню: камень давит ящерицу.\n" +
            "Ящерица одолеет бумагу: ящерица ест бумагу.\n" +
            "Ящерица проиграет ножницам: ножницы отрезают голову ящерице.\n" +
            "Ящерица одолеет спока: ящерица травит спока.\n")

    println("При выборе спока:\n" +
            "Спок одолеет камень: спок испаряет камень.\n" +
            "Спок проиграет бумаге: бумага подставляет спока.\n" +
            "Спок одолеет ножницы: спок ломает ножницы.\n" +
            "Спок проиграет ящерице: ящерица травит спока.\n")

    println("Пожалуйста, для начала игры введите Ваше имя: ")
    var playAgain = true
    var userName = readLine() ?: ""

    while (playAgain) {
        println("$userName, давайте начнём игру?")
        println("Пожалуйста, сделайте свой выбор и введите номер одного из пяти вариантов:\n" +
                "1 - камень;\n" +
                "2 - ножницы;\n" +
                "3 - бумага;\n" +
                "4 - ящерица;\n" +
                "5 - спок.")
        var input: String
        do {
            input = readLine()?.trim() ?: ""
            if (input !in listOf("1", "2", "3", "4", "5")) {
                println("Неверный ввод, повторите попытку.")
            }
        } while (input !in listOf("1", "2", "3", "4", "5"))

        Game.usersStart(input)
        Game.programsStart()
        println("$userName, Ваш выбор: ${Game.variants[Game.usersChoice.toInt() - 1]}")
        println("Выбор программы: ${Game.programsChoice}")
        println("Результат игры: ${Game.defineWinner()}")
        println("\nЕсли желаете выйти из игры, пожалуйста, введите 'в', 'выход' или 'выйти'.\n" +
                "Если хотите начать сначала, нажмите Enter.")
        do {
            input = readLine()?.trim() ?: ""
            if (input !in listOf("в", "выход", "выйти", "")) {
                println("Неверный ввод, повторите попытку.")
            }
        } while (input !in listOf("в", "выход", "выйти", ""))
        playAgain = input.isEmpty()
    }
    println("$userName, спасибо за игру! Будем ждать Вас снова!")
}

object Game {
    var usersChoice: String = ""
    var programsChoice: String = ""
    val variants = listOf("камень", "ножницы", "бумага", "ящерица", "спок")
    private var winResult: String = ""

    fun usersStart(userInput: String) {
        usersChoice = userInput
    }

    fun programsStart() {
        val random = SecureRandom()
        programsChoice = variants[random.nextInt(variants.size)]
    }

    fun defineWinner(): String {
        winResult = when (usersChoice) {
            "1" -> rockCondition()
            "2" -> scissorsCondition()
            "3" -> paperCondition()
            "4" -> lizardCondition()
            "5" -> spockCondition()
            else -> "Ошибка"
        }
        return winResult
    }

    private fun rockCondition(): String {
        return when (programsChoice) {
            "камень" -> "ничья!"
            "бумага", "спок" -> "поражение!"
            "ножницы", "ящерица" -> "победа!"
            else -> "Ошибка"
        }
    }

    private fun scissorsCondition(): String {
        return when (programsChoice) {
            "ножницы" -> "ничья!"
            "камень", "спок" -> "поражение!"
            "бумага", "ящерица" -> "победа!"
            else -> "Ошибка"
        }
    }

    private fun paperCondition(): String {
        return when (programsChoice) {
            "бумага" -> "ничья!"
            "ножницы", "ящерица" -> "поражение!"
            "камень", "спок" -> "победа!"
            else -> "Ошибка"
        }
    }

    private fun lizardCondition(): String {
        return when (programsChoice) {
            "ящерица" -> "ничья!"
            "камень", "ножницы" -> "поражение!"
            "бумага", "спок" -> "победа!"
            else -> "Ошибка"
        }
    }

    private fun spockCondition(): String {
        return when (programsChoice) {
            "спок" -> "ничья!"
            "бумага", "ящерица" -> "поражение!"
            "камень", "ножницы" -> "победа!"
            else -> "Ошибка"
        }
    }
}