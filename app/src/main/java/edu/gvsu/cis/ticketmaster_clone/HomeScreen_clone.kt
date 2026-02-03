package edu.gvsu.cis.ticketmaster_clone

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.shape.RoundedCornerShape // Add this import
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    //variables for segmented buttons
    val options = listOf("Location", "Dates")
    var selectedIndex by remember { mutableIntStateOf(0) }

    val textFieldState = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.height(40.dp),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White
                    ),
                    title = {
                        Text(
                            "ticketmaster",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex,
                            label = { Text(label, color = Color.White) },
                            colors = SegmentedButtonDefaults.colors(
                                inactiveContainerColor = Color.Black,
                                activeContentColor = Color.Black,
                                inactiveBorderColor = Color.Black,
                                activeBorderColor = Color.Black
                            ),
                        )
                    }
                }

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                        .semantics { traversalIndex = 0f },
                    shape = RoundedCornerShape(5.dp),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = textFieldState.text.toString(),
                            onQueryChange = {
                                textFieldState.edit { replace(0, length, it) }
                            },
                            onSearch = { expanded = false },
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            placeholder = { Text("Search") }
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    Text("Artist, Event or Venue", modifier = Modifier.padding(16.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("Concerts", "Sports", "Arts, Theater & Comedy")
                    categories.forEach { category ->
                        OutlinedButton(
                            shape = RoundedCornerShape(5.dp),
                            onClick = { /* Handle click */ },
                            border = BorderStroke(1.dp, Color.White),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                        ) {
                            Text(category, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // stuff
    }
}