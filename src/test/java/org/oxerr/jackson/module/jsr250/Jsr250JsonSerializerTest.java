package org.oxerr.jackson.module.jsr250;

import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ser.BeanSerializer;

class Jsr250JsonSerializerTest {

	private final Jsr250JsonSerializer jsr250JsonSerializer = new Jsr250JsonSerializer(mock(BeanSerializer.class));

	@Test
	void testSerialization() throws IllegalArgumentException, IllegalAccessException {
		Jsr250JsonSerializer deserialized = SerializationUtils.roundtrip(this.jsr250JsonSerializer);
		Object log = FieldUtils.getDeclaredField(Jsr250JsonSerializer.class, "log", true).get(deserialized);
		Assertions.assertNotNull(log);;
	}

}
