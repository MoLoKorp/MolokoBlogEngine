package com.moloko.molokoblogengine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Article(@Id String id, String text){ }
