package jayho.oneday.serializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AvroSerializer <T> implements Serializer<T> {

    private final BinaryEncoder encoder;
    private final DatumWriter<T> datumWriter;
    private final ByteArrayOutputStream out;

    public AvroSerializer(Class<T> clazz) {
        datumWriter = new SpecificDatumWriter<>(clazz);
        out = new ByteArrayOutputStream();
        encoder = EncoderFactory.get().binaryEncoder(out, null);
    }


    @Override
    public byte[] serialize(String topic, T data) {
        byte[] result;
        try {
            datumWriter.write(data, encoder);
            encoder.flush();
            result = out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
