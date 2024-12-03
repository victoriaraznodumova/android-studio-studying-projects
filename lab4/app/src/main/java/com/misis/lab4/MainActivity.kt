package com.misis.lab4


import NoteActivity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.misis.lab4.ui.theme.Lab4Theme

class MainActivity : ComponentActivity() {

    private val isHomeInitialize = mutableStateOf(false)
    private val isNoteInitialize = mutableStateOf(false)
    private lateinit var home : HomeActivity
    private lateinit var note : NoteActivity




    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavigationGraph(navController: NavHostController)
    {
        NavHost(navController, startDestination = Destinations.HomeScreen.route)
        {
            composable(Destinations.HomeScreen.route)
            {
                isNoteInitialize.value = false
                if(!isHomeInitialize.value)
                {
                    isHomeInitialize.value = true
                    home = HomeActivity(navController)
                }
                home.HomeActivityScreen() // будет подсвечиваться красным, это нормально
            }
            composable(Destinations.NoteScreen.route + "/{note_id}", arguments = listOf(navArgument("note_id"){ type = NavType.StringType }))
            {
                isHomeInitialize.value = false
                if(!isNoteInitialize.value)
                {
                    isNoteInitialize.value = true
                    note = NoteActivity(navController, it.arguments?.getString("note_id")!!)
                }
                note.NoteActivityScreen() // будет подсвечиваться красным, это нормально
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab4Theme {
                val navController: NavHostController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background)
                {
                    Box(modifier = Modifier.background(Color.White))
                    {
                        NavigationGraph(navController)
                    }
                }
            }
        }
    }



    @Preview
    @Composable
    fun PreviewHomeActivityScreen(){
        val navController = rememberNavController()
        HomeActivity(navController).HomeActivityScreen()
    }
}