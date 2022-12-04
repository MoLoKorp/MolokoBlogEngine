package com.moloko.molokoblogengine.model;

import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User of the api with particular role.
 *
 * @param username used as an ID
 * @param password not empty
 * @param role ADMIN or USER
 */
@Document
public record User(@Id String username, @NotNull String password, @NotNull String role) {}
