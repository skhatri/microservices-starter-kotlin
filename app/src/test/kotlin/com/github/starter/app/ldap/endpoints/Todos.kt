package com.github.starter.app.ldap.endpoints

import com.github.starter.app.ldap.model.TodoTask
import java.time.LocalDate
import java.time.LocalDateTime

class Todos {
    private constructor() {
    }

    companion object {
        fun createOne(id: String, dateTime: LocalDateTime): TodoTask {
            return TodoTask(id, "Todo Task 1", "user",
                dateTime, "NEW", dateTime)
        }

        fun createOne(dateTime: LocalDateTime): TodoTask {
            return TodoTask("1", "Todo Task 1", "user",
                dateTime, "NEW", dateTime)
        }

        fun createOneForToday(): TodoTask {
            return createOneForDate(LocalDate.now())
        }

        fun createOneForDate(date: LocalDate): TodoTask {
            return createOne(date.atStartOfDay())
        }
    }


}
