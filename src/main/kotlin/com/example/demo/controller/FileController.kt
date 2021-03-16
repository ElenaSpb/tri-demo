package com.example.demo.controller

import com.example.demo.service.FileProcessService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping
class FileController (val fileProcessService: FileProcessService) {

    @PostMapping
    fun load(@RequestParam(value = "taskFile") file: MultipartFile): ResponseEntity<String> {
        val firstLine = fileProcessService.processFile(file)
        return ResponseEntity.ok().body(firstLine)
    }

    @GetMapping("/get")
    fun get(): ResponseEntity<String> {
        return ResponseEntity.ok().body("Hello from Lenas get rest!")
    }

    @GetMapping
    fun gett(): ResponseEntity<String> {
        return ResponseEntity.ok().body("Hello from Lenas demo!!!")
    }
}