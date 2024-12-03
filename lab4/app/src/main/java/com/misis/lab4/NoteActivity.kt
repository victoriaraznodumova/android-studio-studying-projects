import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.misis.lab4.APISender
import com.misis.lab4.NoteViewModel
import kotlinx.coroutines.*
import okhttp3.Response
import java.time.ZonedDateTime
import java.util.concurrent.CompletableFuture

class NoteActivity(private var navController: NavHostController, private val noteId: String) {



    private val json = jacksonObjectMapper() // Инициализация объекта для десериализации JSON
    val api = APISender() // Инициализация объекта для отправки запросов в бэкенд

    // Массив заметок для главной страницы
    private val notes = mutableStateOf(listOf<NoteViewModel>()) // Инициализация массива заметок
    private val isNotesUploaded = mutableStateOf(false) // Статус загрузки заметок

    // Параметры для заметки, которые могут изменяться
//    private lateinit var noteId: String // Идентификатор заметки
//    private lateinit var title: MutableState<String> // Заголовок заметки
//    private lateinit var content: MutableState<String> // Текст заметки

    private val title = mutableStateOf("") // Заголовок заметки
    private val text = mutableStateOf("") // Текст заметки

    init {
        @Suppress("OPT_IN_USAGE")
        GlobalScope.launch (Dispatchers.IO){
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build() // настройка политики
            StrictMode.setThreadPolicy(policy) // применение политики

            if (noteId != "-1") { // Если noteId != "-1", значит заметка существует
                val noteFuture: CompletableFuture<Response> = api.get("/GetNoteById?id=$noteId") // Запрос заметки по ID
                val noteResponse = noteFuture.get() // Ожидание ответа

                if (noteResponse.isSuccessful) {
                    val note = json.readValue<NoteViewModel>(noteResponse.body?.string().toString()) // Десериализация ответа
                    noteResponse.close()

                    withContext(Dispatchers.Main) {
                        title.value = note.Title // Установка заголовка
                        text.value = note.Text // Установка текста
                        isNotesUploaded.value = true // Обновление состояния
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    title.value = "" // Новая заметка
                    text.value = "" // Новая заметка
                    isNotesUploaded.value = true
                }
            }
        }
    }

    // MutableState для заголовка и текста






    @Composable
    fun NoteActivityScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isNotesUploaded.value) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Заголовок заметки
                    Text(
                        text = if (title.value.isEmpty()) "Заголовок отсутствует" else title.value,
                        fontSize = 25.sp,
                        color = if (title.value.isEmpty()) Color(0xFF9C9C9C) else Color(0xFF222222),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // Текст заметки
                    Text(
                        text = text.value,
                        fontSize = 15.sp,
                        color = Color(0xFF222222),
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip
                    )
                }
            } else {
                // Если данные еще не загружены
                Text(
                    text = "Загрузка заметки...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    color = Color(0xFF9C9C9C)
                )
            }
        }
    }
















//            val notesFuture: CompletableFuture<Response> = api.get("/") // Запрос заметок с сервера
//            val notesResponse = notesFuture.get() // Ожидание ответа
//
//            if (notesResponse.isSuccessful) {
//                val notesList = json.readValue<List<NoteViewModel>>(notesResponse.body?.string().toString()) // обработка тела ответа на запрос с последующей десериализацией
//                notesResponse.close() // Закрытие ответа на запрос. Если не закрыть - поток не будет остановлен и приложение может завершиться с ошибкой
//                // Сортировка заметок по дате обновления в порядке убывания
//                val sortedNotes = notesList.sortedByDescending { ZonedDateTime.parse(it.UpdatedAt) }
//                withContext(Dispatchers.Main) {
//                    notes.value = sortedNotes // Обновление состояния заметок
//                    isNotesUploaded.value = true // Показываем, что заметки были загружены
//                }
//            }









        }



































//    @Composable
//    fun NoteActivityScreen() {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)        ) {
//            // Надпись "Заметка"
//                Text(
//                    text = "Заметка",
//                    fontSize = 24.sp,
//                    color = Color.Black,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )            // Заголовок заметки с рамкой
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color(0xFFF5F4F2),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .padding(8.dp)            )
//                {
//                    BasicTextField(
//                        value = title.value,
//                        onValueChange = { newText -> title.value = newText },
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        textStyle = androidx.compose.ui.text.TextStyle(
//                            fontSize = 20.sp,
//                            color = Color(0xFF404040)
//                        ),
//                        decorationBox = { innerTextField ->
//                            Box {
//                                if (title.value.isEmpty()) {
//                                    Text(
//                                        text = "Введите заголовок",
//                                        fontSize = 20.sp,
//                                        color = Color(0xFF9C9C9C)
//                                    )
//                                }
//                                innerTextField()
//                            }
//                        }
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))            // Текст заметки с рамкой
//                Box(
//                       modifier = Modifier
//                           .fillMaxWidth()
//                           .heightIn(min = 200.dp)
//                           .background(Color(0xFFF5F4F2), shape = RoundedCornerShape(8.dp))
//                           .padding(8.dp)
//                ) {
//                    BasicTextField(
//                        value = content.value,
//                        // переменную text заменила на content
//                        onValueChange = { newText -> content.value = newText },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(min = 200.dp),
//                        textStyle = androidx.compose.ui.text.TextStyle(
//                            fontSize = 16.sp,
//                            color = Color(0xFF404040)
//                        ),
//                        decorationBox = { innerTextField ->
//                            Box {
//                                if (content.value.isEmpty()) {
//                                    Text(
//                                        text = "Введите текст заметки",
//                                        fontSize = 16.sp,
//                                        color = Color(0xFF9C9C9C)
//                                    )
//                                }
//                                innerTextField()
//                            }
//                        }
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))            // Кнопка сохранения заметки
//            Button(
//                onClick = {
////                    saveNote()
//                    navController.navigateUp()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//            ) {
//                Text(text = "Сохранить")
//            }
//            }
//        }
//    }










