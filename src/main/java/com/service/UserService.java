package com.service;

import com.constant.dto.UserLoginDTO;
import com.entity.User;

public interface UserService {
    User getUser(UserLoginDTO userLoginDTO);
}
