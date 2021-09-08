import com.expediagroup.graphql.server.types.GraphQLServerResponse
import graphql.GraphqlServer
import graphql.mapper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.quarkus.runtime.Startup
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@Startup
@ApplicationScoped
class Server {
    fun onStart(@Observes se: StartupEvent) {
        embeddedServer(Netty, port = 8000) {
            routing {
                post("graphql") {
                    val qlResponse: GraphQLServerResponse? = GraphqlServer.parser.execute(call.request)
                    if (qlResponse != null) {
                        // write response as json
                        val json = mapper.writeValueAsString(qlResponse)
                        call.respond(json)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    }
                }
                get("playground") {
                    val playgroundHtml = Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
                    if (playgroundHtml != null) {
                        call.respondText(playgroundHtml
                            .replace("\${graphQLEndpoint}", "graphql")
                            .replace("\${subscriptionsEndpoint}", "graphql"), contentType = ContentType.Text.Html)
                    } else {
                        call.respondText("Playground html not generated")
                    }
                }
            }
        }.start()
    }
}
