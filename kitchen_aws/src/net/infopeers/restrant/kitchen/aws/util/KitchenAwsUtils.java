package net.infopeers.restrant.kitchen.aws.util;

import java.util.TimeZone;

import javax.persistence.EntityManagerFactory;

import net.infopeers.restrant.kitchen.aws.jpa.simplejpa.EntityManagerFactoryBuilder;
import net.infopeers.restrant.kitchen.jpa.DB;

import com.spaceprogram.simplejpa.util.AmazonSimpleDBUtil;

public class KitchenAwsUtils {

	public static AwsInitializer init(String unitName, String lobBacketName) {
		// For SimpleJPA Store Date
		AmazonSimpleDBUtil.setTimeZone(TimeZone.getTimeZone("GMT"));
	
		AwsInitializer init = new AwsInitializer();
		init.initialize();
		EntityManagerFactoryBuilder builder = init
				.createEntityManagerFactoryBuilder();
		builder.setUnitName(unitName);
		builder.setLobBucketName(lobBacketName);
		EntityManagerFactory factory = builder.build();
	
		DB.setEntityManagerFactory(factory);
		return init;
	}

}
