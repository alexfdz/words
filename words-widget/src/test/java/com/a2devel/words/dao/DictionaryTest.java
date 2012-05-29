package com.a2devel.words.dao;

import java.io.IOException;

import junit.framework.Assert;
import mt.rcasha.dict.client.DictException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2devel.words.to.Word;

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
		String word = dictionary.getRandomWord();
		Assert.assertNotNull(word);
		
		String definition = dictionary.getTranslation(word);
		Assert.assertNotNull(definition);
		logger.info("definition : " + definition);
	}
	
	
	@Test
	public void testGetWords() throws DictException, IOException{
		for (int i = 0; i < 10; i++) {
			Word wordWrapper = dictionary.getWord();
			Assert.assertNotNull(wordWrapper);
			
			String word = wordWrapper.getWord();
			String definition = wordWrapper.getTranslation();
			
			Assert.assertNotNull(word);
			Assert.assertNotNull(definition);
			Assert.assertTrue(word.length() > 0);
			Assert.assertTrue(definition.length() > 0);
			
			logger.info("test #" + i + "\n\tword: "+word + "\n\tdefinition : " + definition);
		}
	}


}
