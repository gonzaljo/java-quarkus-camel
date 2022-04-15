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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                .routeId("ch.dlj.firstRoute")
                .description("Read from a csv file, unmarshal, convert, filter and write as json")
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
                .process(exchange -> {
                    List<PersonOut> outs = exchange.getIn().getBody(List.class);

                    List<PersonOut> personOuts = outs.stream().sorted(Comparator
                            .comparing(PersonOut::getBirthday)).collect(Collectors.toList());

                    exchange.getIn().setBody(personOuts);
                })
                .marshal().json(JsonLibrary.Jackson, true)
                .to(config.toDirectory().replace('#', '$'));
    }
}

@ConfigMapping(prefix = "ch.vmsolutions.dlj.routeconfig")
interface FileCodingConfig {

    String fromFile();
    String toDirectory();
}
