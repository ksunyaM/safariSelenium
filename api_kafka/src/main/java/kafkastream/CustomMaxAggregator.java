package kafkastream;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

public class CustomMaxAggregator implements Processor<String, Long> {

    /**
     * The following example demonstrates how to use the test driver and helper classes.
     * The example creates a topology that computes the maximum value per key using a key-value-store.
     * While processing, no output is generated, but only the store is updated.
     * Output is only sent downstream based on event-time and wall-clock punctuations.
     */


    ProcessorContext context;
    private KeyValueStore<String, Long> store;

    @SuppressWarnings("unchecked")
    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        context.schedule(60000, PunctuationType.WALL_CLOCK_TIME, time -> flushStore());
        context.schedule(10000, PunctuationType.STREAM_TIME, time -> flushStore());
        store = (KeyValueStore<String, Long>) context.getStateStore("aggStore");
    }

    @Override
    public void process(String key, Long value) {
        Long oldValue = store.get(key);
        if (oldValue == null || value > oldValue) {
            store.put(key, value);
        }
    }

    private void flushStore() {
        KeyValueIterator<String, Long> it = store.all();
        while (it.hasNext()) {
            KeyValue<String, Long> next = it.next();
            context.forward(next.key, next.value);
        }
    }

    @Override
    public void close() {
    }
}

