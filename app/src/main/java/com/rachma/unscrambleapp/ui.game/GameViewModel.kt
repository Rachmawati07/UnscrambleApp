package com.rachma.unscrambleapp.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

// Untuk mendeklarasikan class GameViewModel
// ViewModel yang berisi data aplikasi dan metode untuk memproses data
class GameViewModel : ViewModel(){

    // Mendeklarasikan variabel yang dapat diubah yang hanya dapat dimodifikasi di dalam kelas dideklarasikan.
    // Untuk mendapatkan skor
    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    // Untuk mendeklarasikan daftar kata yang digunakan pada permainan ini
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    // Untuk mendeklarasikan function yang bernama getNextWord
    // Untuk memperbarui kata saat ini dan kata acak yang tampil saat ini dengan kata selanjutnya
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    // Untuk menginisialisasi ulang daftar kata pada permainan untuk memulai ulang permainan
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    // Untuk mendeklarasikan function increaseScore()
    // Untuk meningkatkan skor permainan jika kata yang dituliskan pemain benar
    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    // Untuk mengembalikan nilai true jika kata yang dituliskan pemain benar.
    // Untuk meningkatkan skor yang sesuai
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    // Untuk mengembalikan nilai true jika kata saat ini kurang dari maksimal nomor dari kata yang telah ditentukan
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
}
