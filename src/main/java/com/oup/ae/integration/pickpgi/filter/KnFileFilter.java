/**
 * 
 */
package com.oup.ae.integration.pickpgi.filter;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.springframework.stereotype.Component;

/**
 * @author gunakalc
 *
 */
@Component("knFileFilter")
public class KnFileFilter<T> implements GenericFileFilter<T> {

	@Override
	public boolean accept(GenericFile<T> file) {
		
		return file.getFileName().startsWith("PGI");
	}
	

}
