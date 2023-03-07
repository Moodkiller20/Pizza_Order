package com.example.pizzaorder

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.pizzaorder.R.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var totalAmount : Double = 0.00
    private var subTotalAmount : Double = 0.00
    private var pizzaSize: Double = 0.00
    private var premium: Double = 0.00
    private var toppings: Double = 0.00
    private var deliveryFees: Double = 0.00
    private var tipCharge: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        // List of Pizza to choose from
        val pizzaList = listOf("BBQ Chicken","Pepperoni","Hawaiian (Premium)","Margherita (Premium)")

        // Create an adapter with 3 parameters: activity (this), layout (using a pre-built layout), list to show
        val pizzaAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pizzaList)

        // Store the the listView widget in a variable
        val pizzaSpinner = findViewById<Spinner>(id.pizzaTypeSpinner)

        // set the adapter to the spinner
        pizzaSpinner.adapter = pizzaAdapter

        // set the onItemSelectedListener as (this).  (this) refers to this activity that implements OnItemSelectedListener interface
        pizzaSpinner.onItemSelectedListener = this

        val tipLabel = findViewById<TextView>(id.tipLabel)

        // Listen seekBar change events: There are three override methods that must be implemented
        // though you may not necessarily use the last two
        findViewById<SeekBar>(R.id.tipSeekBar).setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // As the seekbar moves, the progress value is obtained and displayed in our seekBar label
                tipCharge = progress.toDouble()
                tipLabel.text ="$progress %"
                updateAmount()

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    fun updateAmount(){
        // Calculate the percentage
        var tip =totalAmount *(tipCharge/100)


        var totalAmoutLabel = findViewById<TextView>(R.id.totalOrderAmount)
        var subtotalLabel = findViewById<TextView>(R.id.subTotalAmountText)
        var tipLabel = findViewById<TextView>(R.id.tipAmount)

        subTotalAmount = pizzaSize + toppings+premium
        totalAmount = (subTotalAmount+deliveryFees)


        // To fix this issue, you can convert the total price value to print 2 digits regardless what the
        val subtotalPriceNumFormatted:Double = String.format("%.2f", subTotalAmount).toDouble()
        subtotalLabel.text = "\$ $subtotalPriceNumFormatted"

        val tipAmountFormatted:Double = String.format("%.2f", tip).toDouble()
        tipLabel.text = "\$ $tipAmountFormatted"

        val totalPriceNumFormatted:Double = String.format("%.2f", totalAmount+tip).toDouble()
        totalAmoutLabel.text =  "\$ $totalPriceNumFormatted"



    }

    fun popUpBox(view: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Finish order")
        builder.setMessage("Order Now ?")
        builder.setPositiveButton("Yes") { dialog, which ->
            // Handle positive button click, when the user click on Yes
            if(totalAmount>9){
                clearAll()
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "This order can not be completed, Your order is less then $9.00", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // nothing, goes back to the order menu
        }
        builder.show()
    }

    // clear everything
    fun clearAll(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
    // Choose ur pizza
    fun sizeChoiceRadioBtn(view: View) {

        pizzaSize = when (view.id) {
            id.mediumSize ->   9.99
            id.largeSize ->  13.99
            else -> 15.99
        }
        subTotalAmount += pizzaSize
        updateAmount()
    }
    // delivery choice
    fun deliverySwicth(view: View) {
        // check if switch is checked
        val delivery = findViewById<Switch>(id.deliveryFeesSwitch)
        val switchText: String
        if (delivery.isChecked) {
            switchText = "Yes, $2.00"
            deliveryFees = 2.00

        } else {
            switchText = "No, $0.00"
            deliveryFees -=2.00

        }
        // show a custom message on the switch
        delivery.text = switchText
        updateAmount()
    }

    // Topping choice
    fun checkBoxClick(view: View) {
        if(view is CheckBox){
            val checked = view.isChecked
            if(view.id == R.id.tomatoeCheckBox){
                if(checked){
                    toppings +=1.69
                    updateAmount()
                } else {
                    toppings -=1.69
                    updateAmount()
                }
            }
            else if(view.id == R.id.mushroomCheckBox){
                if(checked){
                    toppings +=1.69

                    updateAmount()
                } else {
                    toppings -=1.69
                    updateAmount()
                }
            }
            else if(view.id == R.id.onionCheckBox){
                if(checked){
                    toppings +=1.69

                    updateAmount()
                } else {
                    toppings -=1.69

                    updateAmount()
                }
            }
            else if(view.id == R.id.spinachCheckBox){
                if(checked){
                    toppings +=1.69

                    updateAmount()
                } else {
                    toppings -=1.69
                    updateAmount()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Determine which item in the dropdown list is selected
        val selectedItem = parent?.getItemAtPosition(position).toString()


        // Based on the index of position selected, set the corresponding image
        val imageId = when(position){
            0 -> drawable.bbq_chicken
            1 -> drawable.pepperoni
            2 -> drawable.margheritta
            else -> drawable.hawaiian
        }

        // if oone of the premium pizza is selected
        if(position ==2  || position==3){
            premium =2.00
            updateAmount()
        }
        else{
            premium =0.00
            updateAmount()
        }
        // Set the image resource based on selected imageId
        findViewById<ImageView>(R.id.pizzaImageView).setImageResource(imageId)

    }
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}