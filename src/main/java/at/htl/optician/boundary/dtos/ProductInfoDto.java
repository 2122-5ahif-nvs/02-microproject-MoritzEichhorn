package at.htl.optician.boundary.dtos;

public class ProductInfoDto {
    private Long eanCode;
    private String name;
    private String description;
    private double price;
    private int quantity;

    public ProductInfoDto() {
    }

    public ProductInfoDto(Long eanCode, String name, String description, double price, int quantity) {
        this.eanCode = eanCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getEanCode() {
        return eanCode;
    }

    public void setEanCode(Long eanCode) {
        this.eanCode = eanCode;
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
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
