package kafkastream;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

public class CustomMaxAggregatorSupplier implements ProcessorSupplier {


    @Override
    public Processor get() {
        return new CustomMaxAggregator();
    }
}
