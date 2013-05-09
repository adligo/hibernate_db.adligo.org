package org.adligo.hibernate.db;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AggergrateMappingProvider implements I_HibernateMappingProvider {
	private List<I_HibernateMappingProvider> providers = new ArrayList<I_HibernateMappingProvider>();
	private int size = 0;
	
	public void addProvider(I_HibernateMappingProvider p) {
		providers.add(p);
		size = size + p.size();
	}

	public int size() {
		return size;
	}

	@Override
	public InputStream get(int i) {
		I_HibernateMappingProvider pro = providers.get(0);
		if (pro.size() > i) {
			return pro.get(i);
		}
		int offset = i - pro.size();
		for (int j = 1; j < providers.size(); j++) {
			pro = providers.get(j);
			if (pro.size() > offset) {
				return pro.get(offset);
			} 
			
			offset = offset - pro.size();
		}
		return null;
	}
	
}
