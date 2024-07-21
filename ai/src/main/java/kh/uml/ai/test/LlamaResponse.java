package kh.uml.ai.test;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LlamaResponse {

    private String message;
}