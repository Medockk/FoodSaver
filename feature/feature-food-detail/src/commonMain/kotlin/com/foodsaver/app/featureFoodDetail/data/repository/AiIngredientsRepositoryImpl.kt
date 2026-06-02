package com.foodsaver.app.featureFoodDetail.data.repository

import com.foodsaver.app.featureFoodDetail.data.dto.IngredientAnalyzeDto
import com.foodsaver.app.featureFoodDetail.data.mappers.mapDtoToResponse
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientAnalyzeResponse
import com.foodsaver.app.featureFoodDetail.domain.repository.AiIngredientsRepository
import com.foodsaver.app.utils.HttpConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.String
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.readLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

internal class AiIngredientsRepositoryImpl(
    private val httpClient: HttpClient,
    private val json: Json
) : AiIngredientsRepository {

    override fun analyzeIngredientsByProductId(productId: String): Flow<IngredientAnalyzeResponse?> =
        channelFlow<IngredientAnalyzeResponse?> {

            var emittedAtLeastOnce = false
            val jsonAccumulator = StringBuilder()

            try {
                httpClient.prepareGet(urlString = HttpConstants.AI_URL + "/ingredients/analyze/json") {
                    this.accept(ContentType("application", "x-ndjson"))
                    parameter("productId", productId)
                }.execute { response ->

                    println("LOG: Статус ответа сервера = ${response.status.value}")

                    if (!response.status.isSuccess()) {
                        println("Сервер вернул ошибку: ${response.status}")
                        return@execute
                    }

                    val channel = response.bodyAsChannel()

                    while (true) {
                        val line = channel.readLine()

                        if (line == null) {
                            println("LOG: Сетевой стрим полностью завершен сервером (получен EOF).")
                            break
                        }

                        val trimmedLine = line.trim()

                        // Игнорируем чистый маркдаун
                        if (trimmedLine.startsWith("```")) {
                            continue
                        }

                        if (trimmedLine.isNotEmpty()) {
                            // Накапливаем абсолютно всё, убирая лишние пробелы по краям строки
                            jsonAccumulator.append(trimmedLine)

                            // Запускаем цикл парсинга объектов из накопленного буфера
                            while (true) {
                                val currentBuffer = jsonAccumulator.toString().trim()

                                // Ищем координаты первого валидного JSON-объекта в буфере
                                val startIndex = currentBuffer.indexOf('{')
                                val endIndex = currentBuffer.indexOf('}')

                                // Если нашли и начало, и конец, и они стоят в правильном порядке
                                if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                                    // Вырезаем чистый JSON-объект от { до } включительно
                                    val potentialJson = currentBuffer.substring(startIndex, endIndex + 1)

                                    try {
                                        val dto: IngredientAnalyzeDto = json.decodeFromString(potentialJson)

                                        // Поэтапно отправляем в UI
                                        send(dto.mapDtoToResponse())
                                        emittedAtLeastOnce = true
                                        println("УСПЕХ ПОЭТАПНО: Распарсен ингредиент: ${dto.name}")
                                    } catch (e: Exception) {
                                        // Если внутри {} оказался невалидный или неполный кусок текста,
                                        // выведем лог, чтобы точно знать, что прилетело
                                        println("ОШИБКА СЕРИАЛИЗАЦИИ: ${e.message} | Текст: $potentialJson")
                                    }

                                    // Важно: Удаляем этот обработанный кусок (вместе со скобкой '}') из основного аккумулятора
                                    // Чтобы на следующей итерации внутреннего цикла while парсить то, что идет следом
                                    val remainingText = currentBuffer.substring(endIndex + 1).trim()
                                    jsonAccumulator.setLength(0)
                                    jsonAccumulator.append(remainingText)
                                } else {
                                    // Если цельного объекта { ... } в буфере больше нет,
                                    // выходим из внутреннего цикла и ждем следующую строку из сети через readLine()
                                    break
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("КРИТИЧЕСКОЕ ИСКЛЮЧЕНИЕ СТРИМА: ${e.message}")
                e.printStackTrace()
            } finally {
                if (!emittedAtLeastOnce) {
                    println("Стрим пустой или оборвался, отправляем null")
                    send(null)
                }
            }
        }.flowOn(Dispatchers.Default)
}