package com.reporthub.singleton;

import com.reporthub.service.IReportService;
import com.reporthub.service.IAuthorizationService;
import com.reporthub.service.ITagService;
import com.reporthub.service.IUserService;
import com.reporthub.service.implementation.IReportServiceImpl;
import com.reporthub.service.implementation.IAuthorizationServiceImpl;
import com.reporthub.service.implementation.ITagServiceImpl;
import com.reporthub.service.implementation.IUserServiceImpl;
import lombok.Getter;

public class ServiceSingleton {

    private ServiceSingleton() { }

    @Getter
    private static IUserService userService;

    @Getter
    private static IReportService reportService;

    @Getter
    private static ITagService tagService;

    @Getter
    private static IAuthorizationService authorizationService;

    static {
        userService = new IUserServiceImpl();
        reportService = new IReportServiceImpl();
        tagService = new ITagServiceImpl();
        authorizationService = new IAuthorizationServiceImpl();
    }
}
