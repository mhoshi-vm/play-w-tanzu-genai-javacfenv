package jp.broadcom.tanzu.mhoshi.playwtanzugenaijavacfenv;

import io.pivotal.cfenv.boot.genai.GenaiLocator;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

@RestController
class EmbeddingController {

    EmbeddingModel embeddingModel;

    EmbeddingController(List<GenaiLocator> locators) {
        this.embeddingModel = locators.get(0).getFirstAvailableEmbeddingModel();
    }

    @GetMapping("/ai/embedding")
    Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }
}