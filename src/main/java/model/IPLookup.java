package model;

import javax.persistence.*;

/**
 * Created by alex on 17.19.3.
 */
@Entity
@Table(name = "IP_LOOKUP")
public class IPLookup {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ip;

    @Column(nullable = false)
    private String countryCode;

    public IPLookup(){}

    public IPLookup(String ip, String countryCode){
        this.ip = ip;
        this.countryCode = countryCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
