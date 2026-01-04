
package org.example.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {
        "org.example.ecommerce",
//        "org.example.ecommerce.entity",
//        "org.example.ecommerce.service"
})

@EnableJpaRepositories("org.example.ecommerce.repository")
public class EcommerceApplication {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);

    }


}
