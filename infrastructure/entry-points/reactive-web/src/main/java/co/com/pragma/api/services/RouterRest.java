package co.com.pragma.api.services;

import co.com.pragma.api.config.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@RequiredArgsConstructor
public non-sealed class RouterRest  extends ReportApiDoc{

    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ReportHandler reportHandler) {
        return SpringdocRouteBuilder.route()
                .GET(apiProperties.basePath(),
                        accept(MediaType.APPLICATION_JSON),
                        reportHandler::getApprovedReport,
                        getApprovedReport())
                .build();

    }
}
