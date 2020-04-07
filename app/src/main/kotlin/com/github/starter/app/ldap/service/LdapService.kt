package com.github.starter.app.ldap.service

import reactor.core.publisher.Mono

interface LdapService {
    fun checkMembers(group: String): Mono<List<String>>

    fun getAttribute(): Mono<String>
}
