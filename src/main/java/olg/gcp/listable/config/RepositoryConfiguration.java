package olg.gcp.listable.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"olg.gcp.listable.model"})
@EnableJpaRepositories(basePackages = {"olg.gcp.listable.repository"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}
