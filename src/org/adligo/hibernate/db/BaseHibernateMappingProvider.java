package org.adligo.hibernate.db;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Usage extend and in default constructor do something like;
 * setBasePath("/com/my_company/mappings/");
 * addFile("Batches.xml");
 * addFile("Batchs.xml");
 * @author scott
 *
 */
public abstract class BaseHibernateMappingProvider implements I_HibernateMappingProvider {
	private List<String> files = new ArrayList<String>();
	private String basePath;
	
	protected void addFile(String file) {
		files.add(basePath + file);
	}
	
	@Override
	public int size() {
		return files.size();
	}

	@Override
	public InputStream get(int i) {
		String name = files.get(i);
		if (name != null) {
			InputStream toRet = getInputStream(name);
			return toRet;
		}
		return  null;
		
	}

	public abstract InputStream getInputStream(String fileName);
	
	protected String getBasePath() {
		return basePath;
	}

	protected void setBasePath(String basePath) {
		this.basePath = basePath;
	}

}
