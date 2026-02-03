package edu.gvsu.cis.ticketmaster_clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.gvsu.cis.ticketmaster_clone.ui.theme.TicketMaster_CloneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketMaster_CloneTheme {
                HomeScreen()
                }
            }
        }
    }


//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TicketMaster_CloneTheme {
//        Greeting("Android")
//    }
//}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TicketMaster_CloneTheme {
        HomeScreen()
    }
}