package com.moloko.molokoblogengine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public record Article(@Id String id, @NotNull String text){ }
