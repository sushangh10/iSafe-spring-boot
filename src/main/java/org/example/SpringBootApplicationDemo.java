package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * @Author sushanghai
 * @Date 2021/1/30
 */

@SpringBootApplication
public class SpringBootApplicationDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationDemo.class, args);
    }
}

//@SpringBootApplication
//public class SpringBootApplicationDemo {
//
//    private static final Logger log = LoggerFactory.getLogger(SpringBootApplicationDemo.class);
//
//    public static void main(String[] args) {
//        SpringApplication.run(SpringBootApplicationDemo.class, args);
//    }
//
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
//
//    @Bean
//    public CommandLineRunner run(RestTemplate restTemplate) throws  Exception {
//        return args -> {
//            Quota quota = restTemplate.getForObject("http://localhost:8080/offical", Quota.class, "sushanghai");
//            log.info(quota.toString());
//        };
//    }
//}
