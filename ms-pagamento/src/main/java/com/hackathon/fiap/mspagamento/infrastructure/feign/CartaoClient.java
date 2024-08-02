package com.hackathon.fiap.mspagamento.infrastructure.feign;

import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "cartao", url = "${feign.cartao.url}")
public interface CartaoClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/api/cartao/limite")
    ConsomeLimiteResponseDTO consomeLimite(@RequestHeader("Authorization") String token, @RequestBody ConsomeLimiteRequestDTO consomeLimiteRequestDTO);

}