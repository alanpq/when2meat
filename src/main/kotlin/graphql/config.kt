package graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.execution.GraphQLRequestParser
import com.expediagroup.graphql.server.execution.GraphQLServer
import com.expediagroup.graphql.server.types.GraphQLBatchRequest
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.services.TingMutationService
import graphql.services.TingQueryService
import io.ktor.request.ApplicationRequest
import io.ktor.request.receiveText

val config: SchemaGeneratorConfig = SchemaGeneratorConfig(
    supportedPackages = listOf(
        "graphql.model"
    )
)
val schema = toSchema(
    config,
    queries = arrayListOf(
        TopLevelObject(TingQueryService),
    ),
    mutations = arrayListOf(
        TopLevelObject(TingMutationService),
    ),
    subscriptions = arrayListOf()
)

val graphQL: GraphQL = GraphQL.newGraphQL(schema).build()
val mapper = jacksonObjectMapper()
private val graphQLBatchRequestTypeReference: TypeReference<List<GraphQLRequest>> =
    object : TypeReference<List<GraphQLRequest>>() {}

object GraphqlServer {
    val parser = GraphQLServer(object : GraphQLRequestParser<ApplicationRequest> {
        override suspend fun parseRequest(request: ApplicationRequest): GraphQLServerRequest {
            val rawRequest = request.call.receiveText()
            val jsonNode = mapper.readTree(rawRequest)
            return if (jsonNode.isArray) {
                GraphQLBatchRequest(mapper.convertValue(jsonNode, graphQLBatchRequestTypeReference))
            } else {
                GraphQLRequest(mapper.treeToValue(jsonNode, GraphQLRequest::class.java).query)
            }
        }

    }, object : GraphQLContextFactory<com.expediagroup.graphql.generator.execution.GraphQLContext, ApplicationRequest> {
        override suspend fun generateContext(request: ApplicationRequest): com.expediagroup.graphql.generator.execution.GraphQLContext? {
            return object : com.expediagroup.graphql.generator.execution.GraphQLContext {}
        }
    }, GraphQLRequestHandler(graphQL))
}
