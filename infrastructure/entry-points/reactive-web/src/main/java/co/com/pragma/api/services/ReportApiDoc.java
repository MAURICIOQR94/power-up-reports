package co.com.pragma.api.services;

import co.com.pragma.api.dto.ReportResponseDTO;
import co.com.pragma.api.dto.common.ErrorDTO;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.exampleobject.Builder.exampleOjectBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public sealed class ReportApiDoc permits RouterRest {

    public static final String ACCEPT = "Accept";
    public static final String SUCCESS = "Success";
    public static final String ACCEPT_HEADER = "Accept Header";
    public static final String ACCESS_DENIED = "Not Authenticated";
    public static final String NOT_FOUND = "Report not found";

    protected Consumer<Builder> getApprovedReport() {
        return ops -> ops.tag("reports")
                .operationId("getApprovedReport").summary("Obtain a report of approved loan applications")
                .description("Obtain a report of approved loan applications").tags(new String[]{"reports"})
                .parameter(createHeader(
                        String.class, ACCEPT, ACCEPT_HEADER, APPLICATION_JSON_VALUE
                ))
                .response(responseBuilder().responseCode("200").description(SUCCESS)
                        .content(
                                contentBuilder()
                                        .schema(schemaBuilder().implementation(ReportResponseDTO.class))
                                        .example(exampleFindResponse())
                        )
                )
                .response(responseBuilder().responseCode("401").description(ACCESS_DENIED)
                        .implementation(ErrorDTO.class))
                .response(responseBuilder().responseCode("404").description(NOT_FOUND)
                        .implementation(ErrorDTO.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz,
                                                                              String name,
                                                                              String description,
                                                                              String example) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description)
                .example(example);
    }

    private org.springdoc.core.fn.builders.exampleobject.Builder exampleFindResponse() {
        return exampleOjectBuilder().value("""
                {
                    "totalApproved": 5,
                    "totalAmount": 20500000.0
                }
                """);
    }
}
