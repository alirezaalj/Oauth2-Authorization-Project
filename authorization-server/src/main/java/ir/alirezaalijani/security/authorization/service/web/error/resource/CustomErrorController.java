package ir.alirezaalijani.security.authorization.service.web.error.resource;

import ir.alirezaalijani.security.authorization.service.web.error.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
public class CustomErrorController
        implements ErrorController
{

    @RequestMapping("/api/error")
    public void handleError(@RequestParam(value = "error",defaultValue = "")String statusParam,
                            HttpServletRequest request) {
        if (!statusParam.equals("")){
            int statusCode=Integer.parseInt(statusParam);
            throwFromStatusCode(statusCode);
        }else {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (status != null) {
                int statusCode = Integer.parseInt(status.toString());
                throwFromStatusCode(statusCode);
                return;
            }
        }
        throw new BadRequestException(CustomErrorController.class," : BadRequest.");
    }

    private void throwFromStatusCode(int statusCode) {
        switch (HttpStatus.valueOf(statusCode)) {
            case NOT_FOUND -> throw new PathNotFoundException(CustomErrorController.class, " : path not found.");
            case UNAUTHORIZED -> throw new AccessDeniedException(CustomErrorController.class, " : UNAUTHORIZED.");
            case FORBIDDEN -> throw new ForbiddenException(CustomErrorController.class, " : FORBIDDEN.");
            case INTERNAL_SERVER_ERROR -> throw new InternalServerException(CustomErrorController.class, " : Server error try now.");
            case TOO_MANY_REQUESTS -> throw new TooManyRequestsException(CustomErrorController.class, " : Too Many Requests. Your Blocked!");
            default -> throw new BadRequestException(CustomErrorController.class, " : BadRequest.");
        }
    }

}
