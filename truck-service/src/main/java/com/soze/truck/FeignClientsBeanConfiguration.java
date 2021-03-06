package com.soze.truck;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableFeignClients({"com.soze.common.client"})
@Configuration
@Profile("!test")
public class FeignClientsBeanConfiguration {



}
