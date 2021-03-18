package com.example.demo.service

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQTextMessage
import org.apache.activemq.pool.PooledConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.jms.Connection
import javax.jms.DeliveryMode
import javax.jms.Destination
import javax.jms.Message
import javax.jms.Queue
import javax.jms.Session


@Service
class MessageService {

    @Value("\${wireLevelEndpoint}")
    val wireLevelEndpoint: String = ""

    @Value("\${activeMqUsername}")
    val activeMqUsername: String = ""

    @Value("\${activeMqPassword}")
    val activeMqPassword: String = ""

    lateinit var producerConnection: Connection
    lateinit var consumerConnection: Connection


    @PostConstruct
    fun init() {
        // Create a connection factory.
        val connectionFactory = ActiveMQConnectionFactory(wireLevelEndpoint)

        // Pass the username and password.
        connectionFactory.userName = activeMqUsername
        connectionFactory.password = activeMqPassword

        // Create a pooled connection factory.
        val pooledConnectionFactory = PooledConnectionFactory()
        pooledConnectionFactory.connectionFactory = connectionFactory
        pooledConnectionFactory.maxConnections = 10

        // === Establish a connection for the PRODUCER.
        producerConnection = pooledConnectionFactory.createConnection()
        producerConnection.start()
        sendMessage("LenasQueue1","Init MQ Success!!!")

        // === Establish a connection for the CONSUMER.
        consumerConnection = connectionFactory.createConnection()
        consumerConnection.start()
    }

    fun sendMessage(queueName: String, message: String) {
        val producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val producerDestination: Queue = producerSession.createQueue(queueName)

        val producer = producerSession.createProducer(producerDestination)
        producer.deliveryMode = DeliveryMode.NON_PERSISTENT
        val producerMessage = producerSession.createTextMessage(message)

        producer.send(producerMessage)
        println("Message `$message` was sent to `$queueName`.")
        producer.close()
        producerSession.close()
    }

    fun getMessage(queueName: String): String {
        val consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val consumerDestination: Destination = consumerSession.createQueue(queueName)
        val consumer = consumerSession.createConsumer(consumerDestination)

        val consumerMessage: Message? = consumer.receive(1000)
//        println("Message received: ${consumerMessage.text}")
        println("Message received: ${consumerMessage}")
        consumer.close()
        consumerSession.close()
        return consumerMessage?.toString() ?: "no one message"
    }
}