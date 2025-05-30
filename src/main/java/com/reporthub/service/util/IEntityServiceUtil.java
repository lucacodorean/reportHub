package com.reporthub.service.util;

import com.reporthub.dto.DTO;
import com.reporthub.request.api.v1.IRequest;
import com.reporthub.service.Response;

import java.util.List;

public interface IEntityServiceUtil <T extends DTO, U extends IRequest, V extends IRequest> {
    Response<T> create(U entityStoreRequest);
    Response<T> update(String key, V entityUpdateRequest);
    Response<T> retrieveDTO(String key);

    List<T> all();
}
