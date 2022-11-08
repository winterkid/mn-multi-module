package org.acme.depending;

import org.acme.common.JsonSerdeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
@Property(name = "kafka.enabled", value = "false")
public class FactoryTest {

  @Inject
  public JsonSerdeFactory serdeFactory;

  @Test
  public void testIt() {
    Assertions.assertNotNull(serdeFactory);
  }

}
