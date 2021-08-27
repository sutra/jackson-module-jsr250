package org.oxerr.jackson.module.jsr250;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

public class Jsr250BeanSerializerModifier extends BeanSerializerModifier {

	private final Logger log = LogManager.getLogger();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonSerializer<?> modifySerializer(SerializationConfig config,
			BeanDescription beanDesc, JsonSerializer<?> serializer) {
		if (serializer instanceof BeanSerializer) {
			log.trace("Modifying serializer to RolesAllowedJsonSerializer, for bean type: {}.",
				beanDesc::getBeanClass);
			return new Jsr250JsonSerializer((BeanSerializer) serializer);
		} else {
			return serializer;
		}
	}

}
