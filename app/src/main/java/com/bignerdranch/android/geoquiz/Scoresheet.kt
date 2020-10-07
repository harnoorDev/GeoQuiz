package com.bignerdranch.android.geoquiz

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_scoresheet.*

class Scoresheet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoresheet)

        val intent = getIntent()
        var score = intent.getFloatExtra("Score", 0.00f)



        val pref = getPreferences((Context.MODE_PRIVATE))
        var highscore = pref.getFloat("Score", 0.00f)

if(highscore<score)
{
    highscore = score
    val editor = pref.edit()
    editor.putFloat("Score", score)
    editor.commit()

    usermsg.setText("You set a new high Score!\n Congrats!!!")

}
        else if( highscore == score)
{
    usermsg.setText("You are on level with highscorer!!");
}

        else
{
    usermsg.setText("You couldn't beat the highscore!\n ")

}
        userscore.setText("Your Score: $score")
        savedscore.setText("Highest Score: $highscore")



    }



}
