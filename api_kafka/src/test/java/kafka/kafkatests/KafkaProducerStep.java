package kafka.kafkatests;

import kafkastream.CustomMaxAggregatorSupplier;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.apache.kafka.streams.test.OutputVerifier;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

//import cucumber.api.java.After;
//import cucumber.api.java.Before;

@Ignore("The reason of testing Kafka(implementation details) isn't clear, so get rid of it.")
public class KafkaProducerStep {

    private TopologyTestDriver testDriver;
    private KeyValueStore<String, Long> store;
    private Topology topology = new Topology();
    private StringDeserializer stringDeserializer = new StringDeserializer();
    private LongDeserializer longDeserializer = new LongDeserializer();
    private ConsumerRecordFactory<String, Long> recordFactory;
    //= new ConsumerRecordFactory<>(new StringSerializer(), new NumberSerializers.LongSerializer());

    @Before
    public void setup() {
//        Topology topology = new Topology();
        topology.addSource("sourceProcessor", "input-topic");
        topology.addProcessor("aggregator", new CustomMaxAggregatorSupplier(), "sourceProcessor");
        topology.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.inMemoryKeyValueStore("aggStore"),
                        Serdes.String(),
                        Serdes.Long()).withLoggingDisabled(), // need to disable logging to allow store pre-populating
                "aggregator");
        topology.addSink("sinkProcessor", "result-topic", "aggregator");

        // setup test driver
        Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "maxAggregation");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
//        TopologyTestDriver testDriver = new TopologyTestDriver(topology, props);

        testDriver = new TopologyTestDriver(topology, props);

        // pre-populate store
        store = testDriver.getKeyValueStore("aggStore");
        store.put("a", 21L);








    }



    @After
    public void tearDown() {
        testDriver.close();
    }



    @Test
    public void shouldFlushStoreForFirstInput() {
        recordFactory = new ConsumerRecordFactory("input-topic", new StringSerializer(), new IntegerSerializer());
        testDriver.pipeInput(recordFactory.create("input-topic", "a", 1L, 9999L));
        OutputVerifier.compareKeyValue(testDriver.readOutput("result-topic", stringDeserializer, longDeserializer), "a", 21L);
        Assert.assertNull(testDriver.readOutput("result-topic", stringDeserializer, longDeserializer));
    }














    public void setUpTestDriver() {

        // Processor API

        topology.addSource("sourceProcessor", "input-topic");
//        topology.addProcessor("processor", ...,"sourceProcessor");
        topology.addSink("sinkProcessor", "output-topic", "processor");
// or
// using DSL
//    StreamsBuilder builder = new StreamsBuilder();
//    builder.stream("input-topic").filter(...).to("output-topic");
//    Topology topology = builder.build();

// setup test driver
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        TopologyTestDriver testDriver = new TopologyTestDriver(topology, props);
/**
 * The test driver accepts ConsumerRecords with key and value type byte[].
 * Because byte[] types can be problematic,
 * you can use the ConsumerRecordFactory to generate those records by providing regular Java types
 * for key and values and the corresponding serializers.
 */

//        ConsumerRecordFactory<String, Integer> factory = new ConsumerRecordFactory<>("input-topic", new StringSerializer(), new NumberSerializers.IntegerSerializer());
//       // testDriver.pipe(factory.create("key", 42L));
//        testDriver.pipeInput(factory.create("key", 42L));

/**
 * To verify the output, the test driver produces ProducerRecords with key and value type byte[].
 * For result verification, you can specify corresponding deserializers when reading the output record from the driver.
 */




    }


}