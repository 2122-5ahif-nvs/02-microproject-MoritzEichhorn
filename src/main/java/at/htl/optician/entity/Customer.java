package at.htl.optician.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@XmlRootElement
@Schema(description = "A customer who can buy products")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(required = true)
    private String name;
    private LocalDate birthday;
    @Column(name = "zip_code")
    @Schema(required = true)
    private int zipCode;
    @Schema(required = true)
    private String city;
    @Schema(required = true)
    private String street;

    public Customer(Long id, String name, LocalDate birthday, int zipCode, String city, String street) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
    }

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + birthday + " " + street + " " + zipCode + " " + city;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return zipCode == customer.zipCode && Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(birthday, customer.birthday) && Objects.equals(city, customer.city) && Objects.equals(street, customer.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthday, zipCode, city, street);
    }
}
