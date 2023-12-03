package com.example.otakuizz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.otakuizz.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private val questions: MutableList<Question> = mutableListOf(
        Question(text = "¿Quién es el protagonista de Naruto?",
            answers = listOf("Naruto Uzumaki", "Sasuke Uchiha", "Kakashi Hatake", "Rock Lee")),
        Question(text = "¿Cuál es el nombre del pirata más famoso en One Piece?",
            answers = listOf("Gol D. Roger", "Monkey D. Luffy", "Roronoa Zoro", "Sanji")),
        Question(text = "¿Cómo se llama el titán más grande en Attack on Titan?",
            answers = listOf("Titán Colosal", "Titán Femenino", "Titán Bestia", "Titán Acorazado")),
        Question(text = "¿Quién es el shinigami que conoce Light Yagami en Death Note?",
            answers = listOf("Ryuk", "Rem", "Sidoh", "Jealous")),
        Question(text = "¿Cómo se llama el maestro de Saitama en One Punch Man?",
            answers = listOf("No tiene maestro", "Genos", "Bang", "King")),
        Question(text = "¿Cuál es el verdadero nombre de L en Death Note?",
            answers = listOf("L Lawliet", "Light Yagami", "Ryuzaki", "Hideki Ryuga")),
        Question(text = "¿Quién es el primer Saiyajin en alcanzar el nivel de Super Saiyajin en Dragon Ball?",
            answers = listOf("Goku", "Vegeta", "Trunks", "Gohan")),
        Question(text = "¿Cuál es el nombre del perro en Cowboy Bebop?",
            answers = listOf("Ein", "Spike", "Jet", "Faye")),
        Question(text = "¿Cómo se llama el espíritu que posee a Yugi en Yu-Gi-Oh!?",
            answers = listOf("Atem", "Seto", "Joey", "Tristan")),
        Question(text = "¿Quién es el hermano de Edward Elric en Fullmetal Alchemist?",
            answers = listOf("Alphonse Elric", "Roy Mustang", "Winry Rockbell", "Maes Hughes")),
        Question(text = "¿Cuál es el nombre del protagonista en Sword Art Online?",
            answers = listOf("Kirito", "Asuna", "Yui", "Sinon")),
        Question(text = "¿Cómo se llama el gato en Sailor Moon?",
            answers = listOf("Luna", "Artemis", "Diana", "Serena")),
        Question(text = "¿Quién es el personaje principal en Detective Conan?",
            answers = listOf("Shinichi Kudo", "Ran Mori", "Kogoro Mori", "Heiji Hattori")),
        Question(text = "¿Cuál es el nombre del protagonista en Bleach?",
            answers = listOf("Ichigo Kurosaki", "Rukia Kuchiki", "Orihime Inoue", "Uryū Ishida")),
        Question(text = "¿Cómo se llama el protagonista en My Hero Academia?",
            answers = listOf("Izuku Midoriya", "Katsuki Bakugo", "All Might", "Shoto Todoroki")),
        Question(text = "¿Cuál es el nombre del protagonista en Hunter x Hunter?",
            answers = listOf("Gon Freecss", "Killua Zoldyck", "Kurapika", "Leorio Paradinight")),
        Question(text = "¿Cómo se llama el protagonista en Demon Slayer?",
            answers = listOf("Tanjiro Kamado", "Nezuko Kamado", "Inosuke Hashibira", "Zenitsu Agatsuma")),
        Question(text = "¿Quién es el personaje principal en JoJo's Bizarre Adventure?",
            answers = listOf("Hay varios, cada parte tiene un JoJo diferente", "Jonathan Joestar", "Joseph Joestar", "Jotaro Kujo")),
        Question(text = "¿Cómo se llama el protagonista en Tokyo Ghoul?",
            answers = listOf("Ken Kaneki", "Touka Kirishima", "Hideyoshi Nagachika", "Rize Kamishiro")),
        Question(text = "¿Cuál es el nombre del protagonista en Attack on Titan?",
            answers = listOf("Eren Yeager", "Mikasa Ackerman", "Armin Arlert", "Levi Ackerman"))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)


    private var _binding : FragmentGameBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding.inflate(inflater,container,false)

        //Mezcla las preguntas
        randomizeQuestions()

        // Set the onClickListener for the submitButton
        binding.btnSend.setOnClickListener { _ ->
            val checkedId = binding.rgAnswers.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.rbAnswer2 -> answerIndex = 1
                    R.id.rbAnswer3 -> answerIndex = 2
                    R.id.rbAnswer4 -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        findNavController().navigate(R.id.action_gameFragment_to_gamewonFragment)
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
                }
            }
        }

        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // introduce las respuestas en una copia del array.
        answers = currentQuestion.answers.toMutableList()
        // y las mezcla
        answers.shuffle()
        //update values in views
        binding.tvQuestion.text = currentQuestion.text
        binding.rbAnswer1.text = answers[0]
        binding.rbAnswer2.text = answers[1]
        binding.rbAnswer3.text = answers[2]
        binding.rbAnswer4.text = answers[3]
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name_title, questionIndex + 1, numQuestions)
    }
}

//Clase auxiliar
data class Question(
    val text: String,
    val answers: List<String>)
