package com.example.data

import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val dao: CalculationDao) {
    val history: Flow<List<CalculationEntity>> = dao.getAllHistory()

    suspend fun saveCalculation(expression: String, result: String, isScientific: Boolean) {
        dao.insert(CalculationEntity(expression = expression, result = result, isScientific = isScientific))
    }

    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
