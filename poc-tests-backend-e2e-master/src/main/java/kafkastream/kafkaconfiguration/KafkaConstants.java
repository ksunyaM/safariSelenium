package kafkastream.kafkaconfiguration;

public enum KafkaConstants {

    KAFKA_BROKER("bootstrap.servers"),
    SERIALIZATION("serialization.type"),
    KEY_SERIALIZER("key.serializer"),
    VALUE_SERIALIZER("value.serializer"),
    SCHEMA_REGISTRY_URL("schema.registry.url"),
    MAX_SCHEMAS("max.schemas.per.subject"),
    KAFKA_SSL_ENABLED("kafka.ssl.enabled"),
    KAFKA_PRODUCER("kafka.producer.ssl.properties.path");

    private final String kafkaProducerValue;

    private KafkaConstants(String s) {
        kafkaProducerValue = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return kafkaProducerValue.equals(otherName);
    }

    public String toString() {
        return this.kafkaProducerValue;
    }

}
