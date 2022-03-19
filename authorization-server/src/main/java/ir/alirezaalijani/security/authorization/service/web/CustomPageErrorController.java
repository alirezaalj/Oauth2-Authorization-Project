package ir.alirezaalijani.security.authorization.service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class CustomPageErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,HttpServletResponse response) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String forwardPath = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (forwardPath.startsWith("/api")){
            return "forward:/api/error?error="+status.toString();
        }
        if (status != null) {
            return switch (HttpStatus.valueOf(Integer.parseInt(status.toString()))){
                case NOT_FOUND -> "error/error-404-default";
                case INTERNAL_SERVER_ERROR -> "error/error-500-default";
                case FORBIDDEN -> "error/error-403-default";
//                case UNAUTHORIZED -> "redirect:/auth/login?error=Username Or Password is Wrong";
                case UNAUTHORIZED -> "redirect:/auth/login";
                case TOO_MANY_REQUESTS -> "redirect:/auth/login?error=You are Limited Pleas try after 24 hour";
                default -> "error/error-default";
            };
        }
        return "error/error-default";
    }

    @GetMapping("/error/access-denied")
    public String showAccessDenied() {
        return "error/error-403-default";

    }

}
