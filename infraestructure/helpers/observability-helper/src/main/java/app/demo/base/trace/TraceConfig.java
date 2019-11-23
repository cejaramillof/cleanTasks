package app.demo.base.trace;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:trace.properties")
public class TraceConfig {
}
