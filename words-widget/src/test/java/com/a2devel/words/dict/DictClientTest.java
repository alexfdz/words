package com.a2devel.words.dict;

import java.io.IOException;
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
		Map<String,String> databases = client.getDatabases();
		Assert.assertNotNull(databases);
		Assert.assertFalse(databases.isEmpty());
		for (String key : databases.keySet()) {
			logger.info(key + " : " + databases.get(key));
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
