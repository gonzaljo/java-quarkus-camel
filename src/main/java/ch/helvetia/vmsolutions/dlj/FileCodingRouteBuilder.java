package ch.helvetia.vmsolutions.dlj;

import ch.helvetia.vmsolutions.dlj.dto.PersonIn;
import ch.helvetia.vmsolutions.dlj.dto.PersonOut;
import ch.helvetia.vmsolutions.dlj.eip.PersonOutAggregationStrategy;
import ch.helvetia.vmsolutions.dlj.mapper.PersonMapper;
import io.smallrye.config.ConfigMapping;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FileCodingRouteBuilder extends RouteBuilder {

    @Inject
    PersonMapper mapper;

    @Inject
    FileCodingConfig config;

    @Inject
    PersonOutAggregationStrategy personOutAggregationStrategy;

    @Inject
    Logger logger;

    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat format = new BindyCsvDataFormat(PersonIn.class);
        format.setLocale("de_ch");

        from(config.fromFile())
                .unmarshal(format)
                .split(body())
                .process(exchange -> {
                    PersonIn person = exchange.getIn().getBody(PersonIn.class);
                    PersonOut out = mapper.toPersonOut(person);
                    exchange.getIn().setBody(out);
                })
                .filter(simple("${body.active}"))
                .aggregate(simple("${header.CamelFileName}"), personOutAggregationStrategy)
                    .completionTimeout(100)
                .marshal().json(JsonLibrary.Jackson, true)
                .to(config.toDirectory().replace('#', '$'));
    }
}

@ConfigMapping(prefix = "ch.vmsolutions.dlj.routeconfig")
interface FileCodingConfig {

    String fromFile();
    String toDirectory();
}
