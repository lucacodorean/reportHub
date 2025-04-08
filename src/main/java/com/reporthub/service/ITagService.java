package com.reporthub.service;

import com.reporthub.dto.TagDTO;
import com.reporthub.entity.Tag;
import com.reporthub.request.api.v1.IRequest;
import com.reporthub.request.api.v1.TagStoreRequest;
import com.reporthub.service.util.IEntityServiceUtil;

public interface ITagService extends
        IEntityService<Tag>,
        IEntityServiceUtil<TagDTO, TagStoreRequest, IRequest> { }
