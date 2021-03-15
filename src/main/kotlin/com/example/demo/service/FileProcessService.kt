package com.example.demo.service

import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

@Service
class FileProcessService {

    private val log = logger()

    fun processFile(file: MultipartFile): String {
        log.info(file.name)
        val lines = readOriginalLines(file.inputStream)
        return lines[0]
    }

    fun readOriginalLines(inputStream: InputStream): MutableList<String> {
        val originalLines = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
            .lines()
            .collect(Collectors.toList())
        // inputStream.reset()
        return originalLines
    }
}