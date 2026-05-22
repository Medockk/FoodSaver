package com.foodsaver.app.featureFoodDetail.data.repository

import com.foodsaver.app.commonModule.apiResult.saveApiCall
import com.foodsaver.app.commonModule.apiResult.saveCall
import com.foodsaver.app.featureFoodDetail.data.dto.IngredientAnalyzeDto
import com.foodsaver.app.featureFoodDetail.data.mappers.mapDtoToResponse
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientAnalyzeResponse
import com.foodsaver.app.featureFoodDetail.domain.repository.AiIngredientsRepository
import com.foodsaver.app.utils.HttpConstants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.serverSentEvents
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.readLine
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

internal class AiIngredientsRepositoryImpl(
    private val httpClient: HttpClient,
    private val json: Json
) : AiIngredientsRepository {

    override fun analyzeIngredientsByProductId(productId: String): Flow<IngredientAnalyzeResponse?> =
        flow<IngredientAnalyzeResponse?> {
            try {
                httpClient.prepareGet(urlString = HttpConstants.AI_URL + "/ingredients/analyze/json") {
                    this.accept(ContentType("application", "x-ndjson"))
                    parameter("productId", productId)
                }.execute {

                    if (!it.status.isSuccess()) {
                        println("Сервер вернул ошибку: ${it.status}")
                        emit(null)
                        return@execute
                    }

                    val channel = it.bodyAsChannel()
                    val jsonAccumulator = StringBuilder()

                    while (!channel.isClosedForRead) {
                        val line = channel.readLine() ?: break

                        if (line.isNotBlank()) {
                            // Очищаем строку от маркдаун-мусора, если он прилетел
                            val cleanLine = line
                                .replace("```json", "")
                                .replace("```", "")
                                .trim()

                            if (cleanLine.isNotEmpty()) {
                                jsonAccumulator.append(cleanLine)

                                // Пытаемся распарсить то, что накопили на данный момент
                                try {
                                    val currentString = jsonAccumulator.toString().trim()

                                    // Если строка выглядит как законченный JSON-объект
                                    if (currentString.startsWith("{") && currentString.endsWith("}")) {
                                        val dto: IngredientAnalyzeDto =
                                            json.decodeFromString(currentString)
                                        emit(dto.mapDtoToResponse())
                                        println("Получил DTO с классификацией ингредиентов $dto")

                                        // Очищаем буфер для следующего ингредиента
                                        jsonAccumulator.clear()
                                    }
                                    // Если сервер вернул массив объектов
                                    // и прилетел очередной законченный объект внутри массива
                                    else if (currentString.endsWith("}")) {
                                        // Ищем последний открытый объект
                                        val lastStartIndex = currentString.lastIndexOf('{')
                                        if (lastStartIndex != -1) {
                                            val potentialJson =
                                                currentString.substring(lastStartIndex)
                                            val dto: IngredientAnalyzeDto =
                                                json.decodeFromString(potentialJson)
                                            emit(dto.mapDtoToResponse())

                                            // Удаляем успешно распарсенный кусок из буфера
                                            jsonAccumulator.deleteRange(
                                                lastStartIndex,
                                                currentString.length
                                            )
                                        }
                                    }
                                } catch (e: Exception) {

                                }
                            }
                        }
                    }
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(null)
            }
        }.flowOn(Dispatchers.Default)
}