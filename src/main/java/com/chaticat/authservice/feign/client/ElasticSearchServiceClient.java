package com.chaticat.authservice.feign.client;

import com.chaticat.authservice.feign.payload.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "elastic-search-service", url = "${elastic.search.service.url}")
public interface ElasticSearchServiceClient {

    @Async
    @RequestMapping(method = RequestMethod.POST, value = "/search/users")
    void saveUser(@RequestBody UserRequest request);
}
