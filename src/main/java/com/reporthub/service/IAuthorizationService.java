package com.reporthub.service;

public interface IAuthorizationService {

    public boolean isAdmin(Long id);
    public boolean canDeleteUser(Long authenticatedId, String target);
    public boolean canEditUser(Long authenticatedId, String target);
    public boolean canEditReport(Long authenticatedId, String target);
    public boolean isConnected(Long authenticatedId);
}
