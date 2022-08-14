package com.example.guessinggame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//?: -> Business Logic
class GameViewModel : ViewModel() {
    //?: -> Properties
    private val words = listOf("Android","Activity","Fragment")
    private val secretWord = words.random().uppercase()
    private val _secretWordDisplay = MutableLiveData<String>("")
    private var correctGuess = ""
    private val _incorrectGuesses = MutableLiveData<String>("")
    private val _livesLeft = MutableLiveData<Int>(3)
    private val _gameOver = MutableLiveData<Boolean>(false)

    //?: Backing Property makes the properties to read-only properties
    val secretWordDisplay: LiveData<String>
    get() = _secretWordDisplay

    val incorrectGuesses: LiveData<String>
    get() = _incorrectGuesses

    val livesLeft: LiveData<Int>
    get() = _livesLeft

    val gameOver: LiveData<Boolean>
    get() = _gameOver

    //?: object initializer
    init {secretWordDisplay
        _secretWordDisplay.value = derivedSecretWordDisplay()
    }

    private fun derivedSecretWordDisplay(): String {
        var display = ""

        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(str: String) = when (correctGuess.contains(str)) {
        true -> str
        false -> "_"
    }

    fun makeGuess(guess: String) {
        if (guess.length == 1){
            if(secretWord.contains(guess)){
                correctGuess += guess
                _secretWordDisplay.value = derivedSecretWordDisplay()
            } else {
                _incorrectGuesses.value += "$guess "
                _livesLeft.value = livesLeft.value?.minus(1)
            }
            if(isWon() || isLost()) _gameOver.value = true
        }
    }
    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)

    private fun isLost() = (livesLeft.value ?: 0) <= 0

    fun wonLostMessage() : String {
        var message = ""
        if (isWon())
            message = "You Won!!"
        else if(isLost()){
            message = "You Lost!"
            message += " The word was $secretWord"
        }

        return message
    }

    fun finishGame() {
        _gameOver.value = true
    }

}