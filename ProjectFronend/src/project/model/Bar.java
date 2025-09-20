package project.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement // 标记为XML根元素
public class Bar {
    private int id;
    private String name;
    private String weight;
    private int cals;
    private String manufacturer;

    // 无参构造器（必须）
    public Bar() {}

    // Getters and Setters（全部添加@XmlElement）
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