package com.moloko.molokoblogengine.model;

import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Blog article.
 *
 * @param id ID in UUID format
 * @param text article content
 */
@Document
public record Article(@Id String id, @NotNull String text, @NotNull String owner) {}
