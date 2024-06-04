package com.telus.casa.ai.litellm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LiteLLMProxyAdapter {

	ChatLanguageModel chatLanguageModel;

	public LiteLLMProxyAdapter(ChatLanguageModel chatLanguageModel) {
		this.chatLanguageModel = chatLanguageModel;
	}
	
	public String isBillHigher(String accountNum, String bills, List<String> billMonths) {
		String template = """
		        You are a Bill Analyzer. Answer any questions based on the bill history provided. Provide response in this JSON Format: {"isHigherThanPrevBil": false}
				Bill History:
				{{documents}}
				Question:
				Is the total due of {{month1}} higher than {{month0}} for account number {{accountNum}}
				""";
		PromptTemplate promptTemplate = PromptTemplate.from(template);
		Map<String, Object> variables = new HashMap<>();
		variables.put("documents", bills);
		variables.put("month1", billMonths.get(1));
		variables.put("month0", billMonths.get(0));
		variables.put("accountNum", accountNum);
		
		Prompt prompt = promptTemplate.apply(variables);
		String response = chatLanguageModel.generate(prompt.text());
		log.info("Prompt: " + prompt.text());
		
		log.info("Response: " + response);
		return response;
	}
}