package com.example.demo.controller

import com.example.demo.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.jms.MapMessage

@RestController
class MessageController(val messageService: MessageService) {

    @PostMapping("/message")
    fun load(
        @RequestParam(value = "queue") queue: String,
        @RequestParam(value = "message") message: String,
    ): ResponseEntity<String> {
        messageService.sendMessage(queue, message)
        return ResponseEntity.ok().body("ok")
    }

    @GetMapping("/message")
    fun get(@RequestParam(value = "queue") queue: String): ResponseEntity<String> {
        return ResponseEntity.ok().body(messageService.getMessage(queue))
    }

    @GetMapping("/queue-stat")
    fun getStat(@RequestParam(value = "queue") queue: String): ResponseEntity<String> {
        return ResponseEntity.ok().body(messageService.getQueueStat(queue))
    }

    @GetMapping("/broker-stat")
    fun getBrokerStat(): ResponseEntity<String> {
        return ResponseEntity.ok().body(messageService.getBrokerStat())
    }
}