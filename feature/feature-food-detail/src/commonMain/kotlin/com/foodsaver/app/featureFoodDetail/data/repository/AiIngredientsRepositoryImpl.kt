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

                    while (!channel.isClosedForRead) {
                        val line = channel.readLine() ?: break
                        val trimmedLine = line.trim()

                        // Игнорируем маркдаун-теги
                        if (trimmedLine.startsWith("```")) {
                            continue
                        }

                        if (trimmedLine.isNotEmpty()) {
                            jsonAccumulator.append(trimmedLine)

                            var currentBuffer = jsonAccumulator.toString().trim()

                            // Если бэкенд склеил объекты вида  }{
                            // то временно заменяем их на разделитель, чтобы распарсить по отдельности
                            if (currentBuffer.contains("}{")) {
                                currentBuffer = currentBuffer.replace("}{", "}\n{")
                            }

                            // Разделяем буфер по переносам строк (которые мы сами добавили или они были)
                            val parts = currentBuffer.split("\n")

                            // Пробуем обработать все части, кроме, возможно, последней (если она не дописалась)
                            val itemsToProcess = if (currentBuffer.endsWith("}")) parts else parts.dropLast(1)

                            val processedSuccessfully = mutableListOf<String>()

                            for (part in itemsToProcess) {
                                val cleanPart = part.trim()
                                if (cleanPart.startsWith("{") && cleanPart.endsWith("}")) {
                                    try {
                                        val dto: IngredientAnalyzeDto = json.decodeFromString(cleanPart)

                                        send(dto.mapDtoToResponse())
                                        emittedAtLeastOnce = true
                                        println("УСПЕХ: Распарсен ингредиент: ${dto.name}")

                                        processedSuccessfully.add(part)
                                    } catch (e: Exception) {
                                        // Ошибка парсинга конкретной части, возможно она не полная
                                        println("Ошибка парсинга ${e.message}")
                                    }
                                }
                            }

                            // Удаляем из аккумулятора те части, которые мы успешно распарсили
                            if (processedSuccessfully.isNotEmpty()) {
                                var updatedBuffer = jsonAccumulator.toString()
                                processedSuccessfully.forEach {
                                    updatedBuffer = updatedBuffer.replace(it, "")
                                }
                                jsonAccumulator.setLength(0)
                                jsonAccumulator.append(updatedBuffer.trim())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Если ничего не отправили за весь сеанс — шлем null для безопасности
                if (!emittedAtLeastOnce) {
                    println("Стрим пустой или оборвался, отправляем null")
                    send(null)
                }
            }
        }.flowOn(Dispatchers.Default)
}