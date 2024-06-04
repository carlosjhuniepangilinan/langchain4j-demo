package com.telus.casa.ai.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telus.casa.ai.litellm.LiteLLMProxyAdapter;

import dev.langchain4j.service.spring.AiService;

/**
 * This is an example of using an {@link AiService}, a high-level LangChain4j API.
 */
@RestController
class AssistantController {

    Assistant assistant;
    LiteLLMProxyAdapter litellm;

    AssistantController(Assistant assistant
    		, LiteLLMProxyAdapter litellm
    		) {
        this.assistant = assistant;
        this.litellm = litellm;
    }

    @GetMapping(name = "/assistant", produces = {"application/json"})
    public String assistant(@RequestParam(value = "message", defaultValue = "What is the time now?") String message) {
        return assistant.chat(message);
    }
    
    @PostMapping("/isBillHigherInd/{accountNum}")
    String isBillHigherInd(@PathVariable String accountNum) {
    	String bills = """
    	        Account Number: 600101213
    	        Mar 10, 2024
    	        Account summary Go to telus.com/mytelus for a detailed breakdown of

your monthly charges

Balance forward from your last bill .............................................. $0.00

TELUS Rewards

New charges
view your current balance and redeem, visit Home Phone $0.00 telus.com/rewards
Internet $139.68
TELUS TV $118.10
Other charges and credits $0.00
GST / HST $12.88
Total new charges .................................................................. $270.66
Total due.......................................................$270.66

    	        Account Number: 600101213
    	        Apr 10, 2024
    	        Account summary
Balance forward from your last bill .............................................. $0.00
New charges
Home Phone $0.00
Internet $120.00
GST / HST $6.00
Total new charges .................................................................. $126.00
Total due.......................................................$126.00
    	        """;
    	
    	List<String> billDates = new ArrayList<>();
    	billDates.add("Mar 2024");
    	billDates.add("Apr 2024");
    	
    	return litellm.isBillHigher(accountNum, bills, billDates);
    }
}