package com.misis.lab4

import android.annotation.SuppressLint
import android.os.StrictMode
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.time.ZonedDateTime
import java.util.concurrent.CompletableFuture

@SuppressLint("NewApi")
class HomeActivity(private var navController: NavHostController) {

    private val json = jacksonObjectMapper() // инициализация обьекта для десериализации json
    val api = APISender() // инициализация обьекта для отправки запросов в бэкенд
    private val notes = mutableStateOf(listOf<NoteViewModel>()) // инициализация массива заметок

    //(было принято решение использовать изменяемый наблюдаемый массив, так как при создании заметки необходимо будет добавлять туда эту заметку, а чтобы ее появление можно было увидеть, необходимо, чтобы на изменение этого массива можно было “подписаться”, что и реализуется благодаря MutableState)
    private val isNotesUploaded = mutableStateOf(false) // переменная хранящая статус загрузки всех заметок
    //Служит для того, чтобы дизайн понимал, можно ли уже отображать данные, или же они еще в процессе загрузки. Такие переменные можно делать отдельно для текста и для фото, чтобы дизайн мог отображать текст, когда он загружен, но не отображать картинки, пока они не загрузятся


    init { // функция вызывающаяся при инициализации класса, по сути - конструктор
        @Suppress("OPT_IN_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build() // настройка политики
            StrictMode.setThreadPolicy(policy) // применение политики

            val notesFuture: CompletableFuture<Response> = api.get("/") // Запрос заметок с сервера
            val notesResponse = notesFuture.get() // Ожидание ответа

            if (notesResponse.isSuccessful) {
                val notesList = json.readValue<List<NoteViewModel>>(
                    notesResponse.body?.string().toString()
                ) // обработка тела ответа на запрос с последующей десериализацией
                notesResponse.close() // Закрытие ответа на запрос. Если не закрыть - поток не будет остановлен и приложение может завершиться с ошибкой
                // Сортировка заметок по дате обновления в порядке убывания
                val sortedNotes = notesList.sortedByDescending { ZonedDateTime.parse(it.UpdatedAt) }
                withContext(Dispatchers.Main) {
                    notes.value = sortedNotes // Обновление состояния заметок
                    isNotesUploaded.value = true // Показываем, что заметки были загружены
                }
            }

        }
    }




    //    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun HomeActivityScreen() {
        val notesList by remember { notes }
        val isNotesUploaded by remember { isNotesUploaded }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            ) {
                Text(
                    text = "Заметки",
                    fontSize = 25.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))


            }


            if (isNotesUploaded && notesList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(notesList) { note ->
                        NoteItem(note)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Text(text = "Загрузка заметок...")
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .size(50.dp)
                    .background(Color.Black, shape = RoundedCornerShape(25.dp))
                    .clickable {
                        navController.navigate(Destinations.NoteScreen.route + "/-1") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 25.sp
                )
            }


        }



    }





    @Composable
    fun NoteItem(note: NoteViewModel) {


        Box(
            modifier = Modifier
                .height(100.dp)
                .background(Color(0xFFF5F4F2), shape = RoundedCornerShape(8.dp))
                .padding(vertical = 7.dp, horizontal = 10.dp)
                .clickable {
                    navController.navigate(Destinations.NoteScreen.route + "/${note.Id}") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Текст заголовка
                Text(
                    text = if (note.Title.isEmpty()) "Заголовок отсутствует" else note.Title,
                    color = if (note.Title.isEmpty()) Color(0xFF9C9C9C) else Color(0xFF222222),
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Текст заметки
                Text(
                    text = note.Text,
                    color = Color(0xFF9C9C9C),
                    fontSize = 11.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }



    }


}
