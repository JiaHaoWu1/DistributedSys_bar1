package project.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Bars {
    private List<Bar> bars;
    
    public List<Bar> getBars() { return bars; }
    public void setBars(List<Bar> bars) { this.bars = bars; }
}