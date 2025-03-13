package com.reporthub.singleton;

import com.reporthub.service.*;
import com.reporthub.service.implementation.*;
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
    private static ICommentService commentService;

    @Getter
    private static IAuthorizationService authorizationService;

    @Getter
    private static IMailService mailService;

    static {
        userService = new IUserServiceImpl();
        reportService = new IReportServiceImpl();
        tagService = new ITagServiceImpl();
        commentService = new ICommentServiceImpl();
        authorizationService = new IAuthorizationServiceImpl();
        mailService = new IMailServiceImpl();
    }
}
