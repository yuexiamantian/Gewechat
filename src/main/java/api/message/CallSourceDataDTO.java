package api.message;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CallSourceDataDTO {
    private Integer type = 1;
    private String chatusr;
    private String content;
}