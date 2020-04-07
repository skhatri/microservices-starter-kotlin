package com.github.starter.app.ldap.endpoints

import com.github.starter.app.ldap.model.TodoTask
import com.github.starter.app.ldap.service.LdapService
import com.github.starter.app.ldap.service.TodoService
import com.github.starter.core.container.Container
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/ldap")
class LdapEndpoints(private val ldapService: LdapService) {

    @GetMapping("/search/{group}")
    fun getMembers(@PathVariable("group") group: String): Mono<Container<List<String>>> {
        return ldapService.checkMembers(group).map { Container(it) }
    }

    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAttribute(@RequestBody todoTask: TodoTask): Mono<Container<String>> {
        return ldapService.getAttribute().map { Container(it) }
    }
}
