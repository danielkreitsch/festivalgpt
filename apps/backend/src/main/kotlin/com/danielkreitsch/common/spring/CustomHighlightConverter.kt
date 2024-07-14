package com.danielkreitsch.common.spring

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import org.springframework.boot.ansi.AnsiColor

class CustomHighlightConverter : HighlightingCompositeConverter() {
  override fun getForegroundColorCode(event: ILoggingEvent): String {
    return when (event.level.toInt()) {
      Level.ERROR_INT -> AnsiColor.RED.toString()
      Level.WARN_INT -> AnsiColor.YELLOW.toString()
      else -> AnsiColor.DEFAULT.toString()
    }
  }
}
