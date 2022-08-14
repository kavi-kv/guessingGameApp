package com.example.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.guessinggame.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: GameViewModel //? ->

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container,false)
        val view = binding.root

        //?: Links GameViewModel and GameFragment -> this approach is only used when viewModel class has no arguments
        //?: .. constructor
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //?: Links the data from game to layout
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.gameOver.observe(viewLifecycleOwner,Observer { newValue ->
            if (newValue) {
                val action = GameFragmentDirections.gameToResutlFragment(viewModel.wonLostMessage())
                view.findNavController().navigate(action)
            }
        })

        //?: -> UI Logic
        binding.btnGuessButton.setOnClickListener{
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}