package com.reporthub.singleton;

import com.reporthub.service.IReportService;
import com.reporthub.service.IUserService;
import com.reporthub.service.implementation.IReportServiceImpl;
import com.reporthub.service.implementation.IUserServiceImpl;
import lombok.Getter;

public class ServiceSingleton {

    private ServiceSingleton() { }

    @Getter
    private static IUserService userService;

    @Getter
    private static IReportService reportService;

    static {
        userService = new IUserServiceImpl();
        reportService = new IReportServiceImpl();
    }

}
