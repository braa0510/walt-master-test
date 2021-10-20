package com.walt.model;

import javax.persistence.*;

@Entity
public class Driver extends NamedEntity {

    @ManyToOne
    City city;

    public Driver(){}

    public Driver(String name, City city){
        super(name);
        this.city = city;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Driver)) {
            return false;
        }
        return this.getId().equals(((Driver) obj).getId());

    }
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
