package dataMapped;

import javax.persistence.*;

@Entity
@Table(name = "clients", schema = "public", catalog = "webshop")
public class ClientsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = true, length = -1)
    private String name;
    @Basic
    @Column(name = "address", nullable = true, length = -1)
    private String address;
    @Basic
    @Column(name = "city", nullable = true, length = -1)
    private String city;
    @Basic
    @Column(name = "country", nullable = true, length = -1)
    private String country;
    @Basic
    @Column(name = "adress", nullable = true, length = 255)
    private String adress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
