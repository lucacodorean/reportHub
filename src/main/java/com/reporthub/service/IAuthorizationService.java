package com.reporthub.service;

public interface IAuthorizationService {
    boolean isAdmin(Long id);
    boolean canDeleteUser(Long authenticatedId, String target);
    boolean canEditUser(Long authenticatedId, String target);
    boolean canOperateReport(Long authenticatedId, String target);
    boolean canOperateComment(Long authenticatedId, String target);
    boolean isConnected(Long authenticatedId);
}
