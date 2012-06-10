package com.a2devel.words.to;

import java.io.Serializable;

public class Word implements Serializable{

	private static final long serialVersionUID = 1L;
	private String word;
	private String translation;
	private String wordLanguage;
	private String translationLanguage;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	public String getWordLanguage() {
		return wordLanguage;
	}
	public void setWordLanguage(String wordLanguage) {
		this.wordLanguage = wordLanguage;
	}
	public String getTranslationLanguage() {
		return translationLanguage;
	}
	public void setTranslationLanguage(String translationLanguage) {
		this.translationLanguage = translationLanguage;
	}
}
