package jp.broadcom.tanzu.mhoshi.playwtanzugenaijavacfenv;

import io.pivotal.cfenv.boot.genai.GenaiLocator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class PlayWTanzuGenaiJavacfenvApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayWTanzuGenaiJavacfenvApplication.class, args);
    }

}

@Configuration
class EmbeddingConfig {
    @Bean
    EmbeddingModel embeddingModel(GenaiLocator locator) {
        return locator.getFirstAvailableEmbeddingModel();
    }

    @Bean
    ChatModel chatModel(GenaiLocator locator) {
        return locator.getFirstAvailableChatModel();
    }
}

@RestController
class EmbeddingController {

    EmbeddingModel embeddingModel;

    ChatClient chatClient;

    EmbeddingController(EmbeddingModel embeddingModel, ChatModel chatModel) {
        this.embeddingModel = embeddingModel;
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/ai/embedding")
    Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/ai")
    String generation(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }
}