package RestApi.Service.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class CheckException {
    public static void check(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors= bindingResult.getFieldErrors();
            for(FieldError error: errors){
                errorMsg.append("Error: ").append(error.getField()).append("- ").append(error.getDefaultMessage()).append("; ");
            }
            throw new NotCreatedException(errorMsg.toString());
        }
    }
}
