package com.example.lista_psow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lista_psow.ui.theme.Lista_psowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lista_psowTheme {
                DogListApp()
            }
        }
    }
}

@Composable
fun DogListApp() {
    var textFieldValue by remember { mutableStateOf("") }
    var dogs by remember { mutableStateOf(setOf<String>()) }
    var favoriteDogs by remember { mutableStateOf(setOf<String>()) }
    var isDuplicate by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val filteredDogs = dogs.filter { it.contains(searchQuery, ignoreCase = true) }
    val favoriteDogList = filteredDogs.filter { it in favoriteDogs }
    val nonFavoriteDogList = filteredDogs.filter { it !in favoriteDogs }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        isDuplicate = false
                    },
                    label = { Text("Imię Psa") },
                    isError = isDuplicate,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (textFieldValue.isNotEmpty() && !dogs.contains(textFieldValue)) {
                            dogs = setOf(textFieldValue) + dogs
                            textFieldValue = ""
                            isDuplicate = false
                        } else {
                            isDuplicate = true
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Dodaj Psa")
                }

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Szukaj Pieska"
                    )
                }
            }
            if (isSearchVisible) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Szukaj Pieska") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
            if (isDuplicate) {
                Text(
                    text = "Pies już istnieje!",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "\uD83D\uDC15 : ${dogs.size}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "\u2764  : ${favoriteDogs.size}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn {
                items(favoriteDogList) { dogName ->
                    DogListItem(
                        dogName = dogName,
                        onDelete = {
                            dogs = dogs.minus(dogName)
                            if (dogName in favoriteDogs) {
                                favoriteDogs = favoriteDogs.minus(dogName)
                            }
                        },
                        onFavorite = {
                            if (dogName in favoriteDogs) {
                                favoriteDogs = favoriteDogs.minus(dogName)
                            } else {
                                favoriteDogs = favoriteDogs + dogName
                            }
                        },
                        isFavorite = true
                    )
                }
                items(nonFavoriteDogList) { dogName ->
                    DogListItem(
                        dogName = dogName,
                        onDelete = {
                            dogs = dogs.minus(dogName)
                            if (dogName in favoriteDogs) {
                                favoriteDogs = favoriteDogs.minus(dogName)
                            }
                        },
                        onFavorite = {
                            if (dogName in favoriteDogs) {
                                favoriteDogs = favoriteDogs.minus(dogName)
                            } else {
                                favoriteDogs = favoriteDogs + dogName
                            }
                        },
                        isFavorite = false
                    )
                }
            }
        }
    }
}

@Composable
fun DogListItem(dogName: String, onDelete: () -> Unit, onFavorite: () -> Unit, isFavorite: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .background(Color(0xFFECEFF1))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83D\uDC15",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = dogName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Ulubiony Pies",
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Usuń Psa",
                tint = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DogListPreview() {
    Lista_psowTheme {
        DogListApp()
    }
}


