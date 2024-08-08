package org.elibrary.ntuamultimediaproject.models;

import java.io.Serial;
import java.io.Serializable;

public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
