package com.suplementos.lojasuplementosapi.hateoas;

import org.springframework.hateoas.RepresentationModel;

public class ResourceModel<T> extends RepresentationModel<ResourceModel<T>> {
    
    private T content;
    
    public ResourceModel() {
    }
    
    public ResourceModel(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}
