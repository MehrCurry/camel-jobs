package de.gzockoll.prototype.cameljobs;

import com.google.common.eventbus.Subscribe;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        getContext().setTracing(false);
        onException(Exception.class).redeliveryDelay(1000).maximumRedeliveries(3).useOriginalMessage().to("direct:failed");
        from("direct:failed")
                .marshal().json(JsonLibrary.Gson, true)
                .to("file:failed");
        from("direct:command")
                .marshal().serialization()
                .to("file:commands");
        from("file:commands?delete=true")
                .unmarshal().serialization()
                .choice()
                    .when(simple("${body.isModeFast}")).to("seda:fullSpeed")
                .otherwise()
                    .to("seda:normalSpeed");
        from("seda:fullSpeed?concurrentConsumers=50")
                .to("direct:singleCommand");
        from("seda:normalSpeed")
                .to("direct:singleCommand");
        from("direct:singleCommand")
                .process(ex -> ex.getIn().getBody(Command.class).run())
                .to("guava-eventbus:eventBus");
    }

    @Subscribe
    public void finished(Command aCommand) {
        log.debug("Command has finished: {}", aCommand);
    }
}
