package com.a2devel.words.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import mt.rcasha.dict.client.DefinitionResponse;
import mt.rcasha.dict.client.DictClient;
import mt.rcasha.dict.client.DictException;

public class Dictionary {
	
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

	public String getRandomWord() throws DictException, IOException{
		Map<String,List<String>> matches = dictClient.getMatches(getDatabase(), 
				getStrategy(), 
				String.valueOf((char)(getRandomInt(26) + 'a')));
		
		if(matches.containsKey(getDatabase())){
			List<String> results = matches.get(getDatabase());
			return results.get(getRandomInt(results.size()));
		}
		return null;
	}
	
	public String getDefinition(String word) throws DictException, IOException{
		List<DefinitionResponse> definitions = dictClient.getDefinitions(getDatabase(), word);
		for (DefinitionResponse response : definitions) {
			
		}
		return null;
	}
	
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
