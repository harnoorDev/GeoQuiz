package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel
private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel(){

    init {
        Log.d(TAG,"ViewModel Instance created")

    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"ViewModel instance is about to be destroyed")
    }
    var questionBank = listOf(
        Question (R.string.question_australia,true,false, cheated = false),
        Question (R.string.question_oceans,true,false,cheated = false),
        Question (R.string.question_mideast,false,false,cheated = false),
        Question (R.string.question_africa,false,false,cheated = false),
        Question (R.string.question_americas,true,false,cheated = false),
        Question (R.string.question_asia,true,false,cheated = false))


     var currentIndex = 0
     var score =0
    var isCheater = false

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }



}