package graphql.services

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query


val tings = mutableListOf("scrrrrat")
object TingQueryService : Query {
    fun getTings() = tings
}

object TingMutationService : Mutation {
    fun addTing(tingName: String): List<String> {
        tings.add(tingName)
        return tings
    }
}
