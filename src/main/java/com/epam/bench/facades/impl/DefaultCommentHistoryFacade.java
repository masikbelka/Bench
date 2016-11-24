package com.epam.bench.facades.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.epam.bench.facades.CommentHistoryFacade;
import com.epam.bench.service.dto.bench.CommentHistoryDto;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultCommentHistoryFacade implements CommentHistoryFacade {

    @Override
    public Optional<List<CommentHistoryDto>> getAll(String upsaId) {
        //TODO implement
        return Optional.empty();
    }
}
