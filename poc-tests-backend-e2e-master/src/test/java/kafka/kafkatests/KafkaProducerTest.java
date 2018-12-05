package kafka.kafkatests;

import static org.junit.Assert.fail;

import cucumber.api.java.Before;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import kafkastream.kafkaconfiguration.KafkaLocalServer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Ignore("The reason of testing Kafka(implementation details) isn't clear, so get rid of it.")
@EnableKafka
public class KafkaProducerTest {

    private String topicName;
    private String schemaName;
    private String eventKeySchemaName;
    private int kafkaPartition;
    private int partitionsPerTopic;

    private static String jsonValue = "{\"name\":\"test name9\", \"number1\":9, \"number2\":9.9}";
    private static String jsonKey = "{\"name\":\"test key\"}";
    private static String jsonKeyInt = "{\"number\":9}";
    private static String GROUP_ID = "testSender";

    private Map<String, Object> consumerProperties;
    private Map<String, String> headerMap;
    private Map<String, String> parameters = new HashMap<String, String>();

    private ProducerRecord<Object, Object> record;
    private RecordHeaders headers;

    private static KafkaMessageListenerContainer<Object, Object> container;
    private static BlockingQueue<ConsumerRecord<Object, Object>> records;
    private ConsumerRecord<Object, Object> received;

    private static KafkaLocalServer kafkaLocalServer;
    private static final String DEFAULT_KAFKA_LOG_DIR = "/tmp/test/kafka_embedded";
    private static final String TEST_TOPIC = "test_topic";
    private static final int BROKER_ID = 0;
    private static final int BROKER_PORT = 5000;
    private static final String LOCALHOST_BROKER = String.format("localhost:%d", BROKER_PORT);

    private static final String DEFAULT_ZOOKEEPER_LOG_DIR = "/tmp/test/zookeeper";
    private static final int ZOOKEEPER_PORT = 2000;
    private static final String ZOOKEEPER_HOST = String.format("localhost:%d", ZOOKEEPER_PORT);

    private static final String groupId = "groupID";

    private Charset charset = Charset.forName("UTF-8");
    private CharsetDecoder decoder = charset.newDecoder();
//    @Value("${bootstrap.servers}")
   private String  bootstrapServers = "localhost:9092";

    @Before
    public void startKafka() {

        Properties kafkaProperties;
        Properties zkProperties;

        try {
            //load properties
            kafkaProperties = getKafkaProperties(DEFAULT_KAFKA_LOG_DIR, BROKER_PORT, BROKER_ID);
            zkProperties = getZookeeperProperties(ZOOKEEPER_PORT, DEFAULT_ZOOKEEPER_LOG_DIR);

            //start kafkaLocalServer
            kafkaLocalServer = new KafkaLocalServer(kafkaProperties, zkProperties);
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            fail("Error running local Kafka broker");
            e.printStackTrace(System.out);
        }

    }

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        //produce message here
       // send one message to local kafkaLocalServer server:
        createKafkaTemplate().send(TEST_TOPIC, "test");

    }

    private KafkaTemplate<String, String> createKafkaTemplate() {
        return new KafkaTemplate<>(createProducerFactory());
    }



    private ProducerFactory<String, String> createProducerFactory() {
       return new DefaultKafkaProducerFactory<>(createMapForProducer());
    }


    private Map<String, Object> createMapForProducer() {
        Map<String, Object> kafkaConstants = new HashMap<>();
        kafkaConstants.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaConstants.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        kafkaConstants.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return kafkaConstants;
    }


private static Properties getKafkaProperties(String logDir,int port,int brokerId){
        Properties properties=new Properties();
        properties.put("port",port+"");
        properties.put("broker.id",brokerId+"");
        properties.put("log.dir",logDir);
        properties.put("zookeeper.connect",ZOOKEEPER_HOST);
        properties.put("default.replication.factor","1");
        properties.put("delete.topic.enable","true");
        return properties;
        }


private static Properties getZookeeperProperties(int port,String zookeeperDir){
        Properties properties=new Properties();
        properties.put("clientPort",port+"");
        properties.put("dataDir",zookeeperDir);
        return properties;
        }



        }
