package com.foodsaver.app.coreDb.domain.model

/**
 * Статус синхронизации при изначальном добавлении продукта в локальную БД.
 * @property SYNCHRONIZED Значение 0. Синхронищированно с сервером
 * @property PENDING Значение 1. Отправлен на сервер
 * @property ERROR Значение 2. Произошла какая-то ошибка -> удалить временную запись
 */
enum class SyncStatus {
    SYNCHRONIZED, PENDING, ERROR
}
