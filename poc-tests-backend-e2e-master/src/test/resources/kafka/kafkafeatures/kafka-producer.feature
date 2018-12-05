@kafka
Feature: Sample test for producing Kafka event using Avro serialization type
  This file contains a sample test for producing Kafka event using Avro serialization type

  @smoke
  Scenario: Send an event without headers and key to an existing Kafka topic using default schema for Avro serialization type
    Given An existing topic "testTopic" with 1 partitions
    And An existing default schema "testSchema"
    When I send an event to the topic
    And I receive the event from the topic
    Then I am able to consume the event from the topic


@basic
Scenario: Send an event with only one header and string key to an existing Kafka topic using default schema for Avro serialization type
Given An existing topic "testTopicString" with 1 partitions
And An existing default schema "testSchema"
When I send an event with key using default schema "testKeyString" and header key "TestHeaderKey" with header value "Test Header Value" to the topic at partition 0
And I receive the event from the topic
Then I am able to consume the event with key and one header from the topic

@acceptance
Scenario: Send an event with more headers and integer key to an existing Kafka topic using default schema for Avro serialization type
Given An existing topic "testTopicPartitions" with 3 partitions
And An existing default schema "testSchema"
When I send an event with key using default schema "testKeyInt" to the topic at partition 1 with headers
| HeaderKey      | HeaderValue        |
| TestHeaderKey4 | Test Header Value4 |
| TestHeaderKey5 | Test Header Value5 |
| TestHeaderKey6 | Test Header Value6 |
And I receive the event from the topic
Then I am able to consume the event with key and headers from the topic