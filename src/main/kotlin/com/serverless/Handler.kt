package com.serverless

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Logger
import java.util.*
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.document.Item


class Handler:RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    var tableName = "school-ticket-apis"
    override fun handleRequest(input:Map<String, Any>, context:Context):ApiGatewayResponse {
        val dynamoDB = DynamoDB(getDynamoDbLocalClient())
        val table = dynamoDB.getTable(tableName)
        val item = table.getItem("Id", "dev");
        println(item.toJSONPretty())

        BasicConfigurator.configure()
        LOG.info("received: " + input.keys.toString())

        val responseBody = Response("Go Serverless v1.x! Your Kotlin function executed successfully!", input)
        return ApiGatewayResponse.build {
            statusCode = 200
            objectBody = responseBody
            headers = Collections.singletonMap<String, String>("X-Powered-By", "AWS Lambda & serverless")
        }
    }
    companion object {
        private val LOG = Logger.getLogger(Handler::class.java)
    }

    private fun getDynamoDbLocalClient(): AmazonDynamoDB {
        val client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build()
        return client
    }
}