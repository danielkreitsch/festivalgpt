package de.festivalgpt.backend.service

import jakarta.transaction.Transactional
import java.sql.ResultSet
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class DynamicQueryService(private val jdbcTemplate: JdbcTemplate) {

  @Transactional
  fun executeDynamicQuery(query: String): List<Map<String, Any>> {
    if (!isQuerySafe(query)) {
      throw IllegalArgumentException("Unsafe query detected")
    }

    return jdbcTemplate.query(query) { rs: ResultSet, _: Int ->
      val columns = rs.metaData.columnCount
      val row = mutableMapOf<String, Any>()
      for (i in 1..columns) {
        row[rs.metaData.getColumnName(i)] = rs.getObject(i)
      }
      row
    }
  }

  private fun isQuerySafe(query: String): Boolean {
    val lowercaseQuery = query.lowercase()
    return lowercaseQuery.startsWith("select") &&
        !lowercaseQuery.contains("drop") &&
        !lowercaseQuery.contains("delete") &&
        !lowercaseQuery.contains("truncate") &&
        !lowercaseQuery.contains("alter") &&
        !lowercaseQuery.contains("create")
  }
}
