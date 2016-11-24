package com.epam.bench.facades;

import java.util.List;
import java.util.Optional;

import com.epam.bench.service.dto.bench.CommentHistoryDto;

/**
 * Created by Tetiana_Antonenko1
 */
public interface CommentHistoryFacade {

    Optional<List<CommentHistoryDto>> getAll(String upsaId);
}
