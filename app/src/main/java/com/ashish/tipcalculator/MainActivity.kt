package com.ashish.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.tipcalculator.components.InputField
import com.ashish.tipcalculator.ui.theme.TipCalculatorTheme
import com.ashish.tipcalculator.utils.calculatePerPerson
import com.ashish.tipcalculator.utils.calculatedTip
import com.ashish.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                MyApp {

                }
            }
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 212.0) {
    Surface(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))), color = Color(0xFFE9D7F7)
        // .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun MainContent() {
    BillForm() { billAmount ->
        Log.e("Ashish", "Amount is $billAmount")
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {

        MainContent()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier, onValueChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val noOfPersons = remember {
        mutableStateOf(1)
    }
    val sliderValue = remember {
        mutableStateOf(0f)
    }
    val validateState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val tipAmountValue = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val range = IntRange(start = 1, endInclusive = 100)
    val keyboardController = LocalSoftwareKeyboardController.current
    TopHeader(totalPerPersonState.value)
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(valueState = totalBillState,
                labelId = "Enter bill",
                isEnabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validateState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()
                })
            if (validateState) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Spilt",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if (noOfPersons.value < range.last) {
                                    noOfPersons.value = noOfPersons.value + 1
                                }
                            }
                        )

                        Text(
                            text = noOfPersons.value.toString(),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(
                                    start = 10.dp, end = 10.dp
                                )
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Create,
                            onClick = {
                                noOfPersons.value = if (noOfPersons.value > 1) noOfPersons.value - 1
                                else 1
                            }
                        )
                    }

                }
                //Tip Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = "Tip", modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = modifier.width(200.dp))
                    Text(
                        text = "${tipAmountValue.value}",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    val tip = (sliderValue.value * 100).toInt()
                    Text(text = "$tip%")
                    Spacer(modifier = Modifier.height(10.dp))

                    //Slider
                    Slider(
                        value = sliderValue.value,
                        onValueChange = {
                            sliderValue.value = it
                            tipAmountValue.value =
                                calculatedTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tip
                                )
                            totalPerPersonState.value = calculatePerPerson(
                                totalBillState.value.toDouble(),
                                noOfPersons.value, tip
                            )
                        },
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        steps = 5,
                        onValueChangeFinished = {

                        })
                }

            } else {

            }
        }
    }
}