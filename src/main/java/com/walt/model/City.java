package com.walt.model;

import javax.persistence.Entity;

@Entity
public class City extends NamedEntity {

    public City() {
    }

    public City(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof City)) {
            return false;
        }
        return this.getId().equals(((City) obj).getId());

    }

    @Override
    public String toString() {
        return this.getName();
    }
}
