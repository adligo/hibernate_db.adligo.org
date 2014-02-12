package org.adligo.hibernate.db;

import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.adligo.i.adi.shared.InvocationException;
import org.hibernate.cfg.AvailableSettings;


/**
 * This class is used to build EntityManagerFactories.
 * The new 'official' way of doing this seems to be Persistence.createEntityManagerFactory,
 * however I can't seem to find a way to do that and pass in a DataSource or use the .  
 * For JUnit testing of connection pools, I want to be able to do this so I can
 * pass in DBCP (Apaches Single Threaded DataSource impl), and then switch to c3p0 or something
 * else at runtime.
 * 
 * @author scott
 *
 */
@SuppressWarnings("deprecation")
public class EntityManagerFactoryBuilder {
	public static final String ENTITY_MANAGER_FACTORY_BUILDER_DOES_NOT_ALLOW_NULL_INPUT_STREAMS_IN_THE_MAPPING_PROVIEDER_FOR_MAPPING = "EntityManagerFactoryBuilder does not allow null inputStreams in the MappingProvieder for mapping ";

	public static final String ENTITY_MANAGER_FACTORY_BUILDER_REQUIRES_A_NON_NULL_DATA_SOURCE = 
		"EntityManagerFactoryBuilder requires a non null data source";

	public static final String ENTITY_MANAGER_FACTORY_BUILDER_REQUIRES_NON_NULL_PROPERTIES = 
		"EntityManagerFactoryBuilder requires non null properties";
	public static final String ENTITY_MANAGER_FACTORY_BUILDER_REQUIRES_A_NON_NULL_MAPPING_PROVIDER = 
		"EntityManagerFactoryBuilder requires non null mapping provider";
	/**
	 * ye old dataSource
	 */
	private DataSource dataSource;

	/**
	 * properties for your 
	 */
	private Properties props;
	/**
	 * 
	 */
	private I_HibernateMappingProvider mappingProvider;

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Properties getProps() {
		return props;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	public I_HibernateMappingProvider getMappingProvider() {
		return mappingProvider;
	}
	public void setMappingProvider(I_HibernateMappingProvider mappingProvider) {
		this.mappingProvider = mappingProvider;
	}
	
	public EntityManagerFactory build() throws InvocationException {
		if (props == null) {
			throw new NullPointerException(ENTITY_MANAGER_FACTORY_BUILDER_REQUIRES_NON_NULL_PROPERTIES);
		}
		if (dataSource == null) {
			throw new NullPointerException(ENTITY_MANAGER_FACTORY_BUILDER_REQUIRES_A_NON_NULL_DATA_SOURCE);
		}
		if (mappingProvider == null) {
			throw new NullPointerException(ENTITY_MANAGER_FACTORY_BUILDER_REQUIRES_A_NON_NULL_MAPPING_PROVIDER);
		}
		
		props.setProperty(AvailableSettings.CONNECTION_PROVIDER, 
				"org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider"); 

		HibernateDbConfig config3 = new HibernateDbConfig();
		for (int i = 0; i < mappingProvider.size(); i++) {
			InputStream in = mappingProvider.get(i);
			//add a mapping, I assume hibernate will close the streams correctly for me :)
			if (in == null) {
				throw new NullPointerException(
						ENTITY_MANAGER_FACTORY_BUILDER_DOES_NOT_ALLOW_NULL_INPUT_STREAMS_IN_THE_MAPPING_PROVIEDER_FOR_MAPPING + i);
			}
			config3.addInputStream(in);
		}
		
		config3.setProperties(props);
		config3.setDataSource(dataSource);
		return config3.buildEntityManagerFactory();
	}
}
