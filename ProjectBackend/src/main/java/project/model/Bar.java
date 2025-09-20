package project.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement 
public class Bar {
    private int id;
    private String name;
    private String weight;
    private int cals;
    private String manufacturer;


    public Bar() {}


    @XmlElement
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @XmlElement
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @XmlElement
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    @XmlElement
    public int getCals() { return cals; }
    public void setCals(int cals) { this.cals = cals; }

    @XmlElement
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
}