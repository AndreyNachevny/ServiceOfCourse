package RestApi.Service.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private Long timeOfError;
    private String message;

    public ErrorResponse(Long timeOfError,String message){
        this.timeOfError = timeOfError;
        this.message = message;
    }
}
