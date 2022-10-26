package com.zybooks.diceroller

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zybooks.diceroller.databinding.ActivityMainBinding

const val MAX_DICE = 3

class MainActivity : AppCompatActivity() {

    private var numVisibleDice = MAX_DICE
    private lateinit var diceList: MutableList<Dice>
    private lateinit var diceImageViewList: MutableList<ImageView>
    private lateinit var optionsMenu: Menu
    private var timer: CountDownTimer? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Create list of Dice
        diceList = mutableListOf()
        for (i in 0 until MAX_DICE) {
            diceList.add(Dice(i + 1))
        }

        // Create list of ImageViews
        diceImageViewList = mutableListOf(
            //findViewById(R.id.dice1), findViewById(R.id.dice2), findViewById(R.id.dice3))
        binding.dice1, binding.dice2, binding.dice3)
        showDice()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        optionsMenu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    private fun showDice() {

        // Show visible dice
        for (i in 0 until numVisibleDice) {
            val diceDrawable = ContextCompat.getDrawable(this, diceList[i].imageId)
            diceImageViewList[i].setImageDrawable(diceDrawable)
            diceImageViewList[i].contentDescription = diceList[i].imageId.toString()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Determine which menu option was chosen
        return when (item.itemId) {
            R.id.action_one -> {
                changeDiceVisibility(1)
                showDice()
                true
            }
            R.id.action_two -> {
                changeDiceVisibility(2)
                showDice()
                true
            }
            R.id.action_three -> {
                changeDiceVisibility(3)
                showDice()
                true
            }
            R.id.action_stop -> {
                timer?.cancel()
                item.isVisible = false
                true
            }
            R.id.action_roll -> {
                rollDice()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun rollDice() {
        optionsMenu.findItem(R.id.action_stop).isVisible = true
        timer?.cancel()

        // Start a timer that periodically changes each visible dice
        timer = object : CountDownTimer(2000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                for (i in 0 until numVisibleDice) {
                    diceList[i].roll()
                }
                showDice()
            }

            override fun onFinish() {
                optionsMenu.findItem(R.id.action_stop).isVisible = false
            }
        }.start()
    }

    private fun changeDiceVisibility(numVisible: Int) {
        numVisibleDice = numVisible

        // Make dice visible
        for (i in 0 until numVisible) {
            diceImageViewList[i].visibility = View.VISIBLE
        }

        // Hide remaining dice
        for (i in numVisible until MAX_DICE) {
            diceImageViewList[i].visibility = View.GONE
        }
    }
}