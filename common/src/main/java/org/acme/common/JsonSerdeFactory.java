package org.acme.common;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class JsonSerdeFactory {

  @Inject
  private ObjectMapper objectMapper;

  public <T> Serde<T> create(Class<T> clazz) {
    return new JsonSerde<>(objectMapper, clazz);
  }

  private static class JsonSerde<T> implements Serde<T> {

    private Class<T> clazz;
    private ObjectMapper mapper;

    public JsonSerde(ObjectMapper mapper, Class<T> clazz) {
      this.clazz = clazz;
      this.mapper = mapper;
    }

    @Override
    public Serializer<T> serializer() {
      return new JsonSerializer<T>(mapper);
    }

    @Override
    public Deserializer<T> deserializer() {
      return new JsonDeserializer<T>(mapper, clazz);
    }

  }

  private static class JsonSerializer<T> implements Serializer<T> {

    private ObjectMapper mapper;

    public JsonSerializer(ObjectMapper mapper) {
      this.mapper = mapper;
    }

    @Override
    public byte[] serialize(String topic, T data) {
      try {
        if (data == null) {
          return new byte[] {};
        }
        return mapper.writeValueAsBytes(data);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

  private static class JsonDeserializer<T> implements Deserializer<T> {

    private Class<T> clazz;
    private ObjectMapper mapper;

    public JsonDeserializer(ObjectMapper mapper, Class<T> clazz) {
      this.clazz = clazz;
      this.mapper = mapper;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
      try {
        if (data == null || data.length == 0) {
          return null;
        }
        return mapper.readValue(data, clazz);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
