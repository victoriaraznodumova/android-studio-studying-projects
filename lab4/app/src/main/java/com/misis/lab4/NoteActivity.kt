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
        Box(modifier = Modifier.fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)) {
            if (isNotesUploaded.value) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F4F2), shape = RoundedCornerShape(8.dp))
                            .padding(16.dp) // Отступы внутри поля
                    ) {
                        BasicTextField(
                            value = title.value,
                            onValueChange = { newTitle -> title.value = newTitle },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 20.sp,
                                color = Color(0xFF404040)
                            ),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (title.value.isEmpty()) Text(
                                        text = "Введите заголовок",
                                        fontSize = 20.sp,
                                        lineHeight = 32.sp,
                                        color = Color(0xFF9C9C9C)
                                    )
                                    innerTextField()
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F4F2), shape = RoundedCornerShape(8.dp))
                            .padding(16.dp) // Отступы внутри поля
                    ) {
                        // Поле для ввода текста
                        BasicTextField(
                            value = text.value,
                            onValueChange = { newText -> text.value = newText },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 200.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                color = Color(0xFF404040)
                            ),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (text.value.isEmpty()) Text(
                                        text = "Введите текст заметки",
                                        fontSize = 16.sp,
                                        color = Color(0xFF9C9C9C)
                                    )
                                    innerTextField()
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    // Кнопка для сохранения заметки
                    Button(
                        onClick = {
                            saveNote() // Вызов функции сохранения
                            navController.navigateUp() // Возврат на предыдущую страницу
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Сохранить")
                    }
                }
            } else {
                Text(
                    text = "Загрузка заметки...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    color = Color(0xFF9C9C9C)
                )
            }







//
//
//
//
//                    // Заголовок заметки
//                    Text(
//                        text = if (title.value.isEmpty()) "Заголовок отсутствует" else title.value,
//                        fontSize = 25.sp,
//                        color = if (title.value.isEmpty()) Color(0xFF9C9C9C) else Color(0xFF222222),
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Spacer(modifier = Modifier
//                        .height(20.dp)
//                    )
//                    // Текст заметки
//                    Text(
//                        text = text.value,
//                        fontSize = 15.sp,
//                        color = Color(0xFF222222),
//                        maxLines = Int.MAX_VALUE,
//                        overflow = TextOverflow.Clip
//                    )
//                }
//            } else {
//                // Если данные еще не загружены
//                Text(
//                    text = "Загрузка заметки...",
//                    modifier = Modifier.align(Alignment.Center),
//                    fontSize = 18.sp,
//                    color = Color(0xFF9C9C9C)
//                )
//            }
        }
    }






    private fun saveNote() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseFuture = if (noteId == "-1") {
                    // Если это новая заметка
                    api.post("/CreateNote?title=${title.value}&text=${text.value}")
                } else {
                    // Если заметка уже существует
                    api.post("/EditNote?id=$noteId&title=${title.value}&text=${text.value}")
                }

                // Ожидание результата
                val response = responseFuture.get()

                // Проверка успешности через содержимое строки или другие критерии
                if (response.contains("success", ignoreCase = true)) {
                    println("Заметка успешно сохранена")
                } else {
                    println("Ошибка при сохранении заметки: $response")
                }
            } catch (e: Exception) {
                println("Ошибка: ${e.message}")
            }
        }
    }
    }



























































//    @Composable
//
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//            ) {
//                Text(text = "Сохранить")
//            }
//            }
//        }
//    }
