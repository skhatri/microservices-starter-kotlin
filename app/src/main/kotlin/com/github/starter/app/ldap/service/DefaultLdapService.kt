package com.github.starter.app.ldap.service

import com.github.starter.app.config.LdapConfig
import org.apache.directory.api.ldap.model.message.SearchScope
import org.apache.directory.ldap.client.api.LdapConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DefaultLdapService(private val ldapConnection: LdapConnection, private val ldapConfig: LdapConfig) : LdapService {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DefaultLdapService::class.java)
    }

    override fun checkMembers(group: String): Mono<List<String>> {
        val searchResult = ldapConnection.search(ldapConfig.baseDn, "(objectclass=*)", SearchScope.ONELEVEL).toList()
        LOGGER.info("search for members in group: {}, number of members = {}", group, searchResult.size)
        val members = searchResult.map{entry ->
            entry.get("cn").string
        }
        return Mono.just(members)
    }

    override fun getAttribute(): Mono<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}