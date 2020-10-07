package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.R.string
import android.R.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent


import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.widget.Toast.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


private const val TAG = "MainActivity"
private const val KEY_RESULT = "result"
private const val QUESTION_COUNT = "count"
private const val ANSWERED_QUES = "anscount"
private const val TOTAL_ANS = "total"
private const val REQUEST_CODE_CHEAT = 0
private const val NoTimesCheated = "cheatcount"
//private var res = "result"
//private var qcount = "count"
private var tques = "total"
private lateinit var cheatButton: Button
private var anscount = IntArray(30)
 var numOfTimesCheated = 0
class MainActivity : AppCompatActivity() {


   private var totalanswers =0
    private var answerIsTrue = false




    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)


        val provider:  ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        cheatButton = findViewById(R.id.cheat_button)
        if(savedInstanceState!=null)
        {
           // res = savedInstanceState.getString(KEY_RESULT).toString()
           //val qcount =savedInstanceState.getInt(QUESTION_COUNT).toString()
            numOfTimesCheated= savedInstanceState.getInt(NoTimesCheated)
          val currentIndex =savedInstanceState?.getInt(QUESTION_COUNT,0)?:0
           val score = savedInstanceState?.getInt(KEY_RESULT,0)?:0
            anscount= savedInstanceState.getIntArray(ANSWERED_QUES)!!
            tques = savedInstanceState.getString(TOTAL_ANS).toString()
            //currentIndex = qcount.toInt()
            quizViewModel.currentIndex = currentIndex
            quizViewModel.score = score
            if( tques == "total")
            {

            }
            else {

                totalanswers = tques.toInt()
            }
        }

        cheatButton.setOnClickListener {
            // Start CheatActivity
            //val intent = Intent(this, CheatActivity::class.java)

            if (numOfTimesCheated == 0) {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                //startActivity(intent)
                startActivityForResult(intent, REQUEST_CODE_CHEAT)

            }

            else
            {
                Toast.makeText(this, "You already cheated once\nCant cheat anymore!", Toast.LENGTH_SHORT).show()
            }
        }

        true_button.setOnClickListener{ view: View ->
          checkAnswer(true)


        }

        imageButtonRight.setOnClickListener {

           // currentIndex = (currentIndex+1) % questionBank.size

            quizViewModel.moveToNext()
            updateQuestion()


        }


        imageButtonLeft.setOnClickListener{

            if(quizViewModel.currentIndex==0)
            {
                quizViewModel.currentIndex =5

                updateQuestion()
            }
            else {
               quizViewModel.currentIndex = (quizViewModel.currentIndex - 1) % quizViewModel.questionBank.size

                updateQuestion()

            }
        }

        false_button.setOnClickListener{view: View ->

           checkAnswer(false)

        }

      updateQuestion()

    }


    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.questionBank[quizViewModel.currentIndex].cheated = quizViewModel.isCheater
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "OnSavedInstance was called")

        //savedInstanceState.putString(KEY_RESULT, res)
       // savedInstanceState.putString(QUESTION_COUNT, qcount)
        savedInstanceState.putInt(QUESTION_COUNT, quizViewModel.currentIndex)
        savedInstanceState.putInt(KEY_RESULT, quizViewModel.score)
        savedInstanceState.putIntArray(ANSWERED_QUES, anscount)
        savedInstanceState.putString(TOTAL_ANS, tques)
        savedInstanceState.putInt(NoTimesCheated, numOfTimesCheated)

    }

   private fun updateQuestion() {


       // val questionTextResId = questionBank[currentIndex].textResId
       val questionTextResId = quizViewModel.currentQuestionText
            question_text_view.setText(questionTextResId)
           //qcount = currentIndex.toString()

     if(quizViewModel.questionBank[quizViewModel.currentIndex].done )//|| anscount[quizViewModel.currentIndex] ==1)
       {
           true_button.isEnabled = false
           false_button.isEnabled = false
       }
       else
       {
           true_button.isEnabled = true
           false_button.isEnabled = true
       }




       if(totalanswers == quizViewModel.questionBank.size)
       {
           var percentage = 100.0*quizViewModel.score / totalanswers
           percentage ="%.2f".format(percentage).toDouble()
           val intent = Intent(this@MainActivity, Scoresheet::class.java)
           intent.putExtra("Score", percentage.toFloat())
           startActivity(intent)
          Toast.makeText(this, "You scored : $percentage %", Toast.LENGTH_SHORT).show()
       }
       if (quizViewModel.currentIndex==1)
       {
          cl.setBackgroundResource(R.drawable.back_ocean)
       }

       if (quizViewModel.currentIndex==0)
       {
           cl.setBackgroundResource(R.drawable.back_australia)
       }

       if(quizViewModel.currentIndex == 2)
       {
           cl.setBackgroundResource(R.drawable.back_redsea)
       }

       if(quizViewModel.currentIndex == 3)
       {
           cl.setBackgroundResource(R.drawable.back_nilr)
       }


       if(quizViewModel.currentIndex == 4)
       {
           cl.setBackgroundResource(R.drawable.back_amazon)
       }

       if(quizViewModel.currentIndex == 5)
       {
           cl.setBackgroundResource(R.drawable.back_baikal)
       }

   }

    private fun checkAnswer(userAnswer : Boolean)
    {
        anscount[quizViewModel.currentIndex] = 1
        totalanswers +=1
        tques = totalanswers.toString()
      //  val correctAnswer = questionBank[currentIndex].answer
          val correctAnswer = quizViewModel.currentQuestionAnswer

        /*
        val messageResId = if(userAnswer == correctAnswer)
        {
            R.string.correct_toast

        }
        else
        {
            R.string.incorrect_toast
        }
        if(userAnswer == correctAnswer)
        {
            quizViewModel.score += 1

        }

         */
        var messageResId = ""
        if(userAnswer != correctAnswer)
        {
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()


        }



        if(quizViewModel.questionBank[quizViewModel.currentIndex].cheated)
        {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show()

        }

        if(userAnswer == correctAnswer)
        {

            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            quizViewModel.score += 1

        }

        var scoredisplaycurrent = quizViewModel.score
        var qnumber = quizViewModel.currentIndex +1
        scoredisplay.setText("Score: \n$scoredisplaycurrent/$qnumber")


        quizViewModel.questionBank[quizViewModel.currentIndex].done = true


      if(quizViewModel.questionBank[quizViewModel.currentIndex].done) //|| anscount[quizViewModel.currentIndex] ==1)
        {
            true_button.isEnabled = false
            false_button.isEnabled = false
        }
        else
        {
            true_button.isEnabled = true
            false_button.isEnabled = true
        }





    }




}
