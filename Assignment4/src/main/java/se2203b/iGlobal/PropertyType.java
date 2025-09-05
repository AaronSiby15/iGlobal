/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.iGlobal;

import javafx.beans.property.*;

/**
 * @author Abdelkader Ouda
 */
public class PropertyType {
    private final StringProperty typeCode;
    private final StringProperty typeName;

    public PropertyType() {
        this.typeCode = new SimpleStringProperty();
        this.typeName = new SimpleStringProperty();
    }
    public PropertyType(String code, String name) {
        this.typeCode = new SimpleStringProperty(code);
        this.typeName = new SimpleStringProperty(name);
    }
    public void setTypeCode(String _code) {
        typeCode.set(_code);
    }
    public void setTypeName(String _code) {
        typeName.set(_code);
    }
    public String getTypeCode() {
        return typeCode.get();
    }
    public String getTypeName() {
        return typeName.get();
    }
    public StringProperty typeCodeProperty() {
        return typeCode;
    }
    public StringProperty typeNameProperty() {
        return typeName;
    }
}
