package org.adligo.hibernate.db;


import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import org.adligo.i.log.shared.Log;
import org.adligo.i.log.shared.LogFactory;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.service.jndi.JndiException;
import org.hibernate.service.jndi.JndiNameException;
import org.hibernate.service.jndi.internal.JndiServiceImpl;

/**
 * @author Emmanuel Bernard
 */
public class NamingHelper {
	private static final Log log = LogFactory.getLog(NamingHelper.class);
	private NamingHelper() {}

	public static void bind(HibernateDbConfig cfg) {
		String name = cfg.getHibernateConfiguration().getProperty( AvailableSettings.CONFIGURATION_JNDI_NAME );
        if ( name == null ) {
        	if (log.isDebugEnabled()) {
        		log.debug( "No JNDI name configured for binding Ejb3Configuration" );
        	}
		}
		else {
			if (log.isDebugEnabled()) {
        		log.debug( "ejb3ConfigurationName " + name);
        	}

			// todo : instantiating the JndiService here is temporary until HHH-6159 is resolved.
			JndiServiceImpl jndiService = new JndiServiceImpl( cfg.getProperties() );
			try {
				jndiService.bind( name, cfg );
				if (log.isDebugEnabled()) {
	        		log.debug( "boundEjb3ConfigurationToJndiName " + name);
	        	}
				try {
					jndiService.addListener( name, LISTENER );
				}
				catch (Exception e) {
					log.error("couldNotBindJndiListener");
				}
			}
			catch (JndiNameException e) {
				log.error("invalidJndiName " + name, e);
			}
			catch (JndiException e) {
				log.error("unableToBindEjb3ConfigurationToJndi ", e);
			}
		}
	}

	private static final NamespaceChangeListener LISTENER = new NamespaceChangeListener() {
		public void objectAdded(NamingEvent evt) {
			if (log.isDebugEnabled()) {
				log.debug("An Ejb3Configuration was successfully bound to name: %s" + 
						evt.getNewBinding().getName());
			}
        }

		public void objectRemoved(NamingEvent evt) {
			String name = evt.getOldBinding().getName();
			log.info("ejb3ConfigurationUnboundFromName " + name);
		}

		public void objectRenamed(NamingEvent evt) {
			String name = evt.getOldBinding().getName();
			log.info("ejb3ConfigurationRenamedFromName " + name);
		}

		public void namingExceptionThrown(NamingExceptionEvent evt) {
			log.error(" unableToAccessEjb3Configuration ", evt.getException());
		}
	};


}
