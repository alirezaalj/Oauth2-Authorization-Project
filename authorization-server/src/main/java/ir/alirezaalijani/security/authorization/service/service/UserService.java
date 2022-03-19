package ir.alirezaalijani.security.authorization.service.service;


import ir.alirezaalijani.security.authorization.service.repository.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    // get user or other info
    User findUserByUsername(String username);
    User findUserByUsernameOrEmail(String username);
    Integer findIdByUsername(String name);
    User findUserByEmail(String email);

    // exist check
    boolean userExistByEmail(String email);
    Boolean userExistByUsername(String username);
    Boolean userExistByUsernameOrEmail(String username,String email);

    // add - update
    void updateUserLastLogin(String name);
    void updateUserPassword(User user, String newPassword);
    void updateUserPasswordByToken(User user, String newPassword);

    // validation info
    User passwordTokenValidation(String token);
    boolean checkUserPasswordMach(User user, String lastPassword);

    // generate
    boolean sendPasswordChangeEmail(User user,String type,String message);

}
