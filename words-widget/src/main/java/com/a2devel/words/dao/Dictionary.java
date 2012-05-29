package com.a2devel.words.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import mt.rcasha.dict.client.DefinitionResponse;
import mt.rcasha.dict.client.DictClient;
import mt.rcasha.dict.client.DictException;
import mt.rcasha.dict.client.Status;
import mt.rcasha.dict.client.StatusException;

import com.a2devel.words.to.Word;

/**
 * @author fernanda
 *
 */
public class Dictionary {
	
	private static final int ATTEMPTS_NOT_MATCH = 3;
	
	private String database;
	private String strategy;
	private DictClient dictClient;
	private Properties properties;

	public Dictionary(String database) throws IOException, DictException{
		this.database = database;
		properties = new Properties();
		properties.load(Dictionary.class.getResourceAsStream("/dictionary.properties"));
		dictClient = new DictClient(properties.getProperty("dictionary.host"));
		strategy = properties.getProperty("dictionary.match_strategy");
	}
	
	/**
	 * @return
	 * @throws DictException
	 * @throws IOException
	 */
	public Word getWord() throws DictException, IOException{
		return getWord(0);
	}
	
	/**
	 * @param currentAttempt
	 * @return
	 * @throws DictException
	 * @throws IOException
	 */
	protected Word getWord(int currentAttempt) throws DictException, IOException{
		Word wordWrapper = null;
		String word = this.getRandomWord();
		String translation = this.getTranslation(word);
		if(translation == null){
			if(currentAttempt == Dictionary.ATTEMPTS_NOT_MATCH){
				return null;
			}else{
				getWord(++currentAttempt);
			}
		}else{
			 wordWrapper = new Word();
			 wordWrapper.setWord(word);
			 wordWrapper.setTranslation(translation);
		}
		return wordWrapper;
	}

	/**
	 * @return
	 * @throws DictException
	 * @throws IOException
	 */
	public String getRandomWord() throws DictException, IOException{
		return getRandomWord(0);
	}
	
	/**
	 * @param currentAttempt
	 * @return
	 * @throws DictException
	 * @throws IOException
	 */
	protected String getRandomWord(int currentAttempt) throws DictException, IOException{
		Map<String,List<String>> matches = null;
		
		try {
			matches = dictClient.getMatches(getDatabase(), getStrategy(), 
					String.valueOf((char)(getRandomInt(26) + 'a')));
		} catch (StatusException e) {
			if(e.getStatus() == Status.ERR_NO_MATCH && 
					currentAttempt < Dictionary.ATTEMPTS_NOT_MATCH){
				return getRandomWord(++currentAttempt);
			}else{
				throw e;
			}
		}
		
		if(matches.containsKey(getDatabase())){
			List<String> results = matches.get(getDatabase());
			return results.get(getRandomInt(results.size()));
		}
		return null;
	}
	
	/**
	 * @param word
	 * @return
	 * @throws DictException
	 * @throws IOException
	 */
	public String getTranslation(String word) throws IOException, DictException{
		String translation = new String();
		List<DefinitionResponse> definitions = null;
		
		try {
			definitions = dictClient.getDefinitions(getDatabase(), word);
		} catch (StatusException e) {
			if(e.getStatus() == Status.ERR_NO_MATCH){
				return null;
			}
		}
		
		for (DefinitionResponse response : definitions) {
			if(getDatabase().equals(response.getDatabase())){
				List<String> lines = response.getLines();
				for (int i = 1; i < lines.size(); i++) {
					if(i > 1){
						translation += "\n";
					}
					translation += lines.get(i);
				}
			}
		}
		
		if(translation.length() == 0){
			return null;
		}
		
		return translation;
	}
	
	/**
	 * @param range
	 * @return
	 */
	private int getRandomInt(int range){
		Random random = new Random();
		return random.nextInt(range);
	}
	
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}
