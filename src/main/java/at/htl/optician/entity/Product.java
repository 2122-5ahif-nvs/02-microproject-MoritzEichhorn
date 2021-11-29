package at.htl.optician.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity
@XmlRootElement
public class Product {
    public static final int EAN_CODE_LENGTH = 13;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ean_code")
    private Long eanCode;
    private String name;
    private String description;
    private double price;
    private int qtyInStock;

    public Product() {
    }

    public Product(Long id, Long eanCode, String name, String description, double price, int qtyInStock) {
        this.id = id;
        setEanCode(eanCode);
        this.name = name;
        this.description = description;
        setPrice(price);
        setQuantity(qtyInStock);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEanCode() {
        return eanCode;
    }

    public void setEanCode(Long eanCode) {
        if(eanCode != null) {
            if(checkEan(eanCode)) {
                this.eanCode = eanCode;
            } else {
                throw new IllegalArgumentException("EanCode is not valid!");
            }
        } else {
            eanCode = null;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price > 0) {
            this.price = price;
        }
    }

    public int getQuantity() {
        return qtyInStock;
    }

    public void setQuantity(int quantity) {
        if(quantity > 0) {
            this.qtyInStock = quantity;
        }
    }

    private boolean checkEan(Long ean)
    {
        if(ean == null) {
            throw new IllegalArgumentException("EanCode is null");
        }

        if(ean.toString().length() != EAN_CODE_LENGTH) {
            throw new IllegalArgumentException("Invalid EanCode length. Expected: " + EAN_CODE_LENGTH + ", Actual: " + ean.toString().length());
        }

        long code = ean;
        int sum = 0;

        for(int i = 0; i < EAN_CODE_LENGTH; i++) {
            if(i % 2 == 0) {
                sum += code % 10;
            } else {
                sum += (code % 10) * 3;
            }
            code /= 10;
        }

        return sum % 10 == 0;
    }

    @Override
    public String toString() {
        return eanCode + " " + name + " " + description + " " + price + " " + qtyInStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && qtyInStock == product.qtyInStock && Objects.equals(eanCode, product.eanCode) && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eanCode, name, description, price, qtyInStock);
    }
}
