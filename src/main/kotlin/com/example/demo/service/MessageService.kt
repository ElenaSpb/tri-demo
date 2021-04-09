package com.example.demo.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.pool.PooledConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Enumeration
import javax.annotation.PostConstruct
import javax.jms.Connection
import javax.jms.DeliveryMode
import javax.jms.Destination
import javax.jms.MapMessage
import javax.jms.Message
import javax.jms.MessageConsumer
import javax.jms.MessageProducer
import javax.jms.Queue
import javax.jms.Session


@Service
class MessageService {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Value("\${wireLevelEndpoint}")
    val wireLevelEndpoint: String = ""

    @Value("\${activeMqUsername}")
    val activeMqUsername: String = ""

    @Value("\${activeMqPassword}")
    val activeMqPassword: String = ""

    lateinit var producerConnection: Connection
    lateinit var consumerConnection: Connection

    lateinit var pooledConnectionFactory: PooledConnectionFactory

    val lenasQueueName = "LenasQueue1"

    @PostConstruct
    fun init() {
        // Create a connection factory.
        val connectionFactory = ActiveMQConnectionFactory(wireLevelEndpoint)

        // Pass the username and password.
        connectionFactory.userName = activeMqUsername
        connectionFactory.password = activeMqPassword

        // Create a pooled connection factory.
        pooledConnectionFactory = PooledConnectionFactory()
        pooledConnectionFactory.connectionFactory = connectionFactory
        pooledConnectionFactory.maxConnections = 10

        // === Establish a connection for the PRODUCER.
        producerConnection = pooledConnectionFactory.createConnection()
        producerConnection.start()
        sendMessage(lenasQueueName, "Init MQ Success!!! ${LocalDateTime.now()}")

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

    fun getBrokerStat(): String {
        val brokerName = "localhost"
        val connection = pooledConnectionFactory.createConnection()
        connection.start()
        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

//        val replyTo: Queue = session.createTemporaryQueue()
        val replyToQueue: Queue = session.createQueue("replyToQueue1")
        val consumer: MessageConsumer = session.createConsumer(replyToQueue)

        val queueBrokerName = "ActiveMQ.Statistics.Broker"
        val brokerQueue: Queue = session.createQueue(queueBrokerName)

        val producer: MessageProducer = session.createProducer(brokerQueue)

        val msg: Message = session.createMessage()
        msg.jmsReplyTo = replyToQueue
        producer.send(msg) // event to write stat

        var answer: Message? = null
        while(answer == null) {
            answer = consumer.receive(1000)
        }

        val reply: MapMessage = answer as MapMessage
        println(reply.toString())

        val e: Enumeration<*> = reply.mapNames
        while (e.hasMoreElements()) {
            val name = e.nextElement().toString()
            println(name + "=" + reply.getObject(name))
        }

        consumer.close()
        producer.close()
        session.close()
        return reply.toString()
    }

    fun getQueueStat(queueName: String): String {
        val session = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)

//        val replyToQueue: Queue = session.createTemporaryQueue()
        val replyToQueue: Queue = session.createQueue("replyToQueue2")

        val producer: MessageProducer = session.createProducer(null)
        val consumer: MessageConsumer = session.createConsumer(replyToQueue)
        val testQueue: Queue = session.createQueue(queueName)

        val fullQueueName = "ActiveMQ.Statistics.Destination." + testQueue.queueName
        val statQueueDestination = session.createQueue(fullQueueName)

        val msg = session.createMessage()
        msg.jmsReplyTo = replyToQueue

        producer.send(testQueue, msg)

        msg.jmsReplyTo = replyToQueue
        producer.send(statQueueDestination, msg)

        val reply = consumer.receive() as MapMessage

        reply.mapNames
        print(reply)
        print(reply.mapNames)

        consumer.close()
        producer.close()
        return reply.jmsCorrelationID
    }

}