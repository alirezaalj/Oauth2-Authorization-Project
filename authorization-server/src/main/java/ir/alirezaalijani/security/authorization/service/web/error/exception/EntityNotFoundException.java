package ir.alirezaalijani.security.authorization.service.web.error.exception;

import ir.alirezaalijani.security.authorization.service.web.error.apierror.CustomErrorMessageGenerator;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz,String message, String... searchParamsMap) {
        super(CustomErrorMessageGenerator.generateMessage(clazz.getSimpleName(),
                message,
                CustomErrorMessageGenerator.toMap(String.class, String.class,  searchParamsMap)
        ));
    }

}
