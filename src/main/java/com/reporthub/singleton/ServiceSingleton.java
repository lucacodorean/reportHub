package com.reporthub.singleton;

import com.reporthub.service.IUserService;
import com.reporthub.service.implementation.IUserServiceImpl;
import lombok.Getter;

public class ServiceSingleton {

    private ServiceSingleton() { }

    @Getter
    private static IUserService userService;

    static {
        userService = new IUserServiceImpl();
    }

}
