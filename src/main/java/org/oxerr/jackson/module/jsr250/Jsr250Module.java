package org.oxerr.jackson.module.jsr250;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson Module for JSR 250.
 */
public class Jsr250Module extends SimpleModule {

	private static final long serialVersionUID = 2016100501L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setupModule(SetupContext context) {
		super.setupModule(context);
		context.addBeanSerializerModifier(new Jsr250BeanSerializerModifier());
	}

}
