package jayho.oneday.deserializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AvroDeserializer <T> implements Deserializer<T> {

    private final DatumReader<T> reader;

    public AvroDeserializer(Class<T> clazz) {
        reader = new SpecificDatumReader<>(clazz);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        T result;
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        try {
            result = reader.read(null, decoder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
