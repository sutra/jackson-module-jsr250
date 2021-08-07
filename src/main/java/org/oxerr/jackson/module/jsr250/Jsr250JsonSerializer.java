package org.oxerr.jackson.module.jsr250;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;

/**
 * Excludes the properties that are {@link DenyAll} or not {@link RolesAllowed}
 * from the serialization output.
 */
public class Jsr250JsonSerializer extends BeanSerializer {

	private static final long serialVersionUID = 2016100501L;

	private final Logger log = LogManager.getLogger();

	public Jsr250JsonSerializer(BeanSerializer serializer) {
		super(serializer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider)
					throws IOException, JsonGenerationException {
		final Collection<? extends GrantedAuthority> grantedAuthorities = this
				.getGrantedAuthorities();

		final BeanPropertyWriter[] props;
		if (_filteredProps != null && provider.getActiveView() != null) {
			props = _filteredProps;
		} else {
			props = _props;
		}
		int i = 0;
		try {
			for (final int len = props.length; i < len; ++i) {
				BeanPropertyWriter prop = props[i];
				if (prop != null) { // can have nulls in filtered list
					final DenyAll denyAll = prop.getAnnotation(DenyAll.class);
					final RolesAllowed rolesAllowed = prop.getAnnotation(RolesAllowed.class);
					if (denyAll != null) {
						log.trace("DenyAll, ignoring {}.", prop);
					} else if (rolesAllowed != null && !this.isAllowed(rolesAllowed.value(), grantedAuthorities)) {
						log.trace("RolesAllowed({}), ignoring {}.", rolesAllowed::value, () -> prop);
					} else {
						prop.serializeAsField(bean, gen, provider);
					}
				}
			}
			if (_anyGetterWriter != null) {
				_anyGetterWriter.getAndSerialize(bean, gen, provider);
			}
		} catch (Exception e) {
			String name = (i == props.length) ? "[anySetter]" : props[i].getName();
			wrapAndThrow(provider, e, bean, name);
		} catch (StackOverflowError e) {
			/* 04-Sep-2009, tatu: Dealing with this is tricky, since we do not
			 *   have many stack frames to spare... just one or two; can't
			 *   make many calls.
			 */
			// 10-Dec-2015, tatu: and due to above, avoid "from" method, call ctor directly:
			//JsonMappingException mapE = JsonMappingException.from(gen, "Infinite recursion (StackOverflowError)", e);
			JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e);

			String name = (i == props.length) ? "[anySetter]" : props[i].getName();
			mapE.prependPath(new JsonMappingException.Reference(bean, name));
			throw mapE;
		}
	}

	protected boolean isAllowed(String[] rolesAllowed, Collection<? extends GrantedAuthority> grantedAuthorities) {
		for (final String role : rolesAllowed) {
			for (final GrantedAuthority grantedAuthority : grantedAuthorities) {
				if (role.equals(grantedAuthority.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}

	protected Collection<? extends GrantedAuthority> getGrantedAuthorities() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null ? authentication.getAuthorities() : Collections.emptyList();
	}

}
