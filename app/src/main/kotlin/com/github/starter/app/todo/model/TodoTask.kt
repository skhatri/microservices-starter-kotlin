package com.github.starter.app.todo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

class TodoTask @JsonCreator constructor(
    @JsonProperty("id") val id: String, @JsonProperty("description") val description: String, @JsonProperty("action_by") val actionBy: String?,
    @JsonProperty("created") val created: LocalDateTime?, @JsonProperty("status") val status: String?, @JsonProperty("updated") val updated: LocalDateTime?) : Serializable {


    override fun toString(): String {
        return StringBuilder("id:").append(id).append(", description: ").append(description).append(", action_by: ").append(actionBy)
            .append(", created: ").append(created).append(", status: ").append(status).append(", updated: ").append(updated).toString();
    }
}
