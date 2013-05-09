package org.adligo.hibernate.db;

import java.io.InputStream;

/**
 * This class provides the xml mapping files one at a time
 * giving a better chance to show what is wrong with them,
 * also hibernate supports interfaces in entities correctly
 * when jpa mappings don't (in my opinion).
 * 
 * @author scott
 *
 */
public interface I_HibernateMappingProvider {
	public int size();
	public InputStream get(int i);
}
