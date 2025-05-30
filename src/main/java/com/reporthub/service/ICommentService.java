package com.reporthub.service;

import com.reporthub.dto.CommentDTO;
import com.reporthub.entity.Comment;
import com.reporthub.request.api.v1.CommentStoreRequest;
import com.reporthub.request.api.v1.CommentUpdateRequest;
import com.reporthub.service.util.IEntityServiceUtil;

public interface ICommentService extends
        IEntityService<Comment>,
        IEntityServiceUtil<CommentDTO, CommentStoreRequest, CommentUpdateRequest> { }
