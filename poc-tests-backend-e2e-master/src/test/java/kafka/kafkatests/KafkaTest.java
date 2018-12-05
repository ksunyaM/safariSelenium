package kafka.kafkatests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore("The reason of testing Kafka(implementation details) isn't clear, so get rid of it.")
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"json:target/reports/kafkaProducer.json"})
public class KafkaTest {

}
