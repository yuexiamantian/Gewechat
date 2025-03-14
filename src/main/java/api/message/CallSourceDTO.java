package api.message;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CallSourceDTO {
    private Integer type = 57;
    private String title;
    private CallSourceDataDTO refermsg;
}
