package example.cashcard;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AuditMetric{

    final Counter counter;

    public AuditMetric(MeterRegistry registry){
        counter = registry.counter("cashcards.hits","createCashCard");
    }

    public void increment(){
        counter.increment();
    }

    public double getUsernameCount() {
        return counter.count();
    }
}
