package by.sadovnick.orderhub.order.config;

import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public DefaultConfigurationCustomizer jooqCustomizer() {
        return (DefaultConfiguration c) -> c.setSettings(new Settings()
// Ключевое: не квотировать имена => ORDER_ITEMS станет order_items на стороне Postgres
                        .withRenderQuotedNames(RenderQuotedNames.NEVER)
// Опционально: если хочешь, чтобы jOOQ всегда писал lower-case
                        .withRenderNameCase(RenderNameCase.LOWER)
        );
    }
}
