package ch.helvetia.vmsolutions.dlj.eip;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PersonOutAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            ArrayList persons = new ArrayList();
            persons.add(newExchange.getIn().getBody());
            newExchange.getIn().setBody(persons);
            return newExchange;
        } else {
            oldExchange.getIn().getBody(List.class).add(newExchange.getIn().getBody());
            return oldExchange;
        }
    }
}
