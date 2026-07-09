package com.daita.datn.services;

import java.util.List;

public interface EmbeddingService {
    List<Double> embedText(String text);
}
