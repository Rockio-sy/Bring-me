package org.bringme.service;

import org.bringme.model.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAll();
    Request getRequestById(Long id);
    Request saveRequest(Request request);
}
