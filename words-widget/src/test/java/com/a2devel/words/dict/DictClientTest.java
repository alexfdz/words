package com.a2devel.words.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;
import mt.rcasha.dict.client.DefinitionResponse;
import mt.rcasha.dict.client.DictClient;
import mt.rcasha.dict.client.DictException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictClientTest {

	
	private DictClient client;
	final Logger logger = LoggerFactory.getLogger(DictClientTest.class);
	
	@Before
	public void initDictClient() throws DictException, IOException{
		client = new DictClient("dict.org");
	}
	@After
	public void closeDictClient() throws DictException, IOException{
		client.finalize();
	}
	
	@Test
	public void testConnection() throws DictException, IOException{
		String help = client.getHelp();
		Assert.assertNotNull(help);
		logger.info(help);
	}
	
	@Test
	public void testShowDatabases() throws DictException, IOException{
		final Map<String,String> databases = client.getDatabases();
		Assert.assertNotNull(databases);
		Assert.assertFalse(databases.isEmpty());
		
		List<String> keys = new ArrayList<String>();
		
		
		for (String key : databases.keySet()) {
			if(key.contains("-")){
				keys.add(key);
			}else{
				logger.info("NOOO: " + key);
			}
		}
		
		Collections.sort(keys,new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				String databaseLhs = databases.get(lhs);
				String databaseRhs = databases.get(rhs);
				return databaseLhs.replace("-", "").compareToIgnoreCase(databaseRhs.replace("-", ""));
			}
		});
		
		System.out.println("KEYS::::::::::::::::");
		for (String key : keys) {
			System.out.println("<item>"+key+"</item>");
		}
		System.out.println("VALUES::::::::::::::::");
		for (String key : keys) {
			System.out.println("<item>"+databases.get(key)+"</item>");
		}
	}
	
	@Test
	public void testDefinition() throws DictException, IOException{
		List<DefinitionResponse> definitions = client.getDefinitions("spa-eng", "coche");
		Assert.assertNotNull(definitions);
		Assert.assertFalse(definitions.isEmpty());
		for (DefinitionResponse definitionResponse : definitions) {
			logger.info(definitionResponse.getTextualInformation());
		}
	}
	
	@Test
	public void testMatch() throws DictException, IOException{
		Random random = new Random();
		Map<String,List<String>> results = client.getMatches("spa-eng", "prefix", String.valueOf((char)(random.nextInt(26) + 'a')));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
		
		for (String key: results.keySet()) {
			List<String> result = results.get(key);
			logger.info("result: " + key);
			for (String string : result) {
				logger.info("\t" + string);
			}
		}
	}
	
	
}
