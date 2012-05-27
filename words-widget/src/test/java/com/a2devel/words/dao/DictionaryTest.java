package com.a2devel.words.dao;

import java.io.IOException;

import junit.framework.Assert;

import mt.rcasha.dict.client.DictException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryTest {
	
	private Dictionary dictionary;
	final Logger logger = LoggerFactory.getLogger(DictionaryTest.class);
	
	@Before
	public void initDictClient() throws DictException, IOException{
		dictionary = new Dictionary("spa-eng");
	}
	
	@Test
	public void testGetRandomWord() throws DictException, IOException{
		String word = dictionary.getRandomWord();
		Assert.assertNotNull(word);
		logger.info("random word : " + word);
	}
	
	@Test
	public void testGetDefinition() throws DictException, IOException{
		String definition = dictionary.getDefinition("coche");
		Assert.assertNotNull(definition);
		logger.info("definition : " + definition);
	}


}
