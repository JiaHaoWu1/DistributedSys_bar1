package project.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import project.model.Bar;
import project.model.Bars;

import java.io.FileWriter;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

public class BarClient {
    private static final String BASE_URL = "http://localhost:8080/ProjectBackend/rest/bars";
    private final Client client;

    public BarClient() {
        this.client = Client.create();
    }

    // 获取所有巧克力棒
    public Bars getAllBars() {
        try {
            WebResource resource = client.resource(BASE_URL);
            String rawResponse = resource.accept(MediaType.APPLICATION_XML).get(String.class);
            System.out.println("后端返回原始数据: " + rawResponse);
            
            Bars bars = resource.accept(MediaType.APPLICATION_XML).get(Bars.class);
            return bars != null ? bars : new Bars();
        } catch (Exception e) {
            System.err.println("API调用异常: " + e.getMessage());
            return new Bars();
        }
    }

    // 根据名称获取单个巧克力棒
    public Bar getBarByName(String name) {
        try {
            WebResource resource = client.resource(BASE_URL + "/" + name);
            return resource.accept(MediaType.APPLICATION_XML).get(Bar.class);
        } catch (Exception e) {
            System.err.println("获取条形数据失败: " + e.getMessage());
            return null;
        }
    }

    // 新增巧克力棒
    public void addBar(Bar bar) {
        WebResource resource = client.resource(BASE_URL);
        ClientResponse response = resource
            .type(MediaType.APPLICATION_FORM_URLENCODED)
            .post(ClientResponse.class,
                "name=" + bar.getName() +
                "&weight=" + bar.getWeight() +
                "&cals=" + bar.getCals() +
                "&manufacturer=" + bar.getManufacturer());
        
        if (response.getStatus() != 201) {
            throw new RuntimeException("添加失败: HTTP " + response.getStatus());
        }
    }

    public void updateBar(int id, String newName) {
        WebResource resource = client.resource(BASE_URL + "/" + id);
        ClientResponse response = resource
            .type(MediaType.APPLICATION_FORM_URLENCODED)
            .put(ClientResponse.class, "newName=" + newName);
        
        if (response.getStatus() != 200) {
            throw new RuntimeException("Update failed: HTTP " + response.getStatus());
        }
    }

    public void updateBar(String currentName, String newName) {
        WebResource resource = client.resource(BASE_URL + "/byName");
        ClientResponse response = resource
            .type(MediaType.APPLICATION_FORM_URLENCODED)
            .put(ClientResponse.class, 
                 "currentName=" + currentName + "&newName=" + newName);
        
        if (response.getStatus() != 200) {
            throw new RuntimeException("Update failed: HTTP " + response.getStatus());
        }
    }
    
    public void exportToExcel(String filePath) {
        try {
            WebResource resource = client.resource(BASE_URL);
            Bars bars = resource.accept(MediaType.APPLICATION_XML).get(Bars.class);
            
            StringBuilder csv = new StringBuilder();
            // 添加表头
            csv.append("ID,Name,Weight,Calories,Manufacturer\n");
            
            // 添加数据行
            if (bars != null && bars.getBars() != null) {
                for (Bar bar : bars.getBars()) {
                    csv.append(bar.getId()).append(",")
                       .append(bar.getName()).append(",")
                       .append(bar.getWeight()).append(",")
                       .append(bar.getCals()).append(",")
                       .append(bar.getManufacturer()).append("\n");
                }
            }
            
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(csv.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Export failed: " + e.getMessage(), e);
        }
    }
    
    public void deleteBar(int id) {
        WebResource resource = client.resource(BASE_URL + "/" + id);
        ClientResponse response = resource.delete(ClientResponse.class);
        
        if (response.getStatus() != 204) {
            throw new RuntimeException("删除失败: HTTP " + response.getStatus());
        }
    }
    
    public void fillTables() {
        WebResource resource = client.resource(BASE_URL + "/fill");
        ClientResponse response = resource.post(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to fill tables: HTTP " + response.getStatus());
        }
    }

    public void createTables() {
        WebResource resource = client.resource(BASE_URL + "/create");
        ClientResponse response = resource.post(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to create tables: HTTP " + response.getStatus());
        }
    }

    public void deleteAllBars() {
        WebResource resource = client.resource(BASE_URL + "/deleteAll");
        ClientResponse response = resource.delete(ClientResponse.class);
        if (response.getStatus() != 204) {
            throw new RuntimeException("Failed to delete all bars: HTTP " + response.getStatus());
        }
    }
}