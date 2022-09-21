package com.rachma.unscrambleapp.ui.game
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rachma.unscrambleapp.R
import com.rachma.unscrambleapp.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Untuk mendeklarasikan class GameFragment dan function Fragment()
// Merupakan tempat dimana permainan dimainkan dan berisi logika
class GameFragment : Fragment() {

    // Untuk membuat instance objek GameViewModel di dalam pengontrol UI yang sesuai
    private val viewModel: GameViewModel by viewModels()

    // Untuk mengikat instance objek dengan akses ke tampilan di layout game_fragment.xml
    private lateinit var binding: GameFragmentBinding

    // Untuk membuat ViewModel saat pertama kali fragmen dibuat dan ketika fragmen dibuat ulang, maka akan menerima instance GameViewModel yang sama yang dibuat oleh fragmen pertama
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Untuk mengikat layout GameFragmant dan mengembalikan data binding
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Untuk menyiapkan klik listener untuk tombol submit dan skip
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Untuk memperbarui kata yang ada pada tampilan
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
            R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    // Untuk mendeklarasikan function onSubmitWord()
    // Untuk memeriksa kata pengguna, dan memperbarui skor yang sesuai dengan hasilnya
    // Untuk menampilkan kata acak selanjutnya
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    // Untuk mendeklarasikan function onSkipWord()
    // Untuk melewati kata yang tampil sekarang tanpa mengubah skor
    // Untuk memperbarui jumlah kata yang akan ditampilkan selanjutnya
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }

    // Untuk mendeklarasikan function getNextScrambleWord()
    // Untuk mendapatkan kata acak untuk daftar kata dan mengacak huruf di dalamnya untuk ditampilkan kembali
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    // Untuk membuat function yang bernama showFinalScoreDialog()
    // Untuk membuat dan menunjukkan dialog alert atau pop up dengan skor akhirnya
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    // Untuk mendeklarasikan function restartGame
    // Inisialisasi ulang data di ViewModel dan perbarui tampilan dengan data baru, untuk mulai ulang permainan.
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }


    // Untuk mendeklarasikan function exitGame()
    // Untuk keluar dari permainan
    private fun exitGame() {
        activity?.finish()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    // Untuk mendeklarasikan function setErrorTextField()
    // Untuk mengatur dan mengatur ulang status kesalahan pada teks
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    // Untuk mendeklarasikan function updateNextWordOnScreen()
    // Untuk menampilkan kata acak selanjutnya
    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }
}
