package project.rest;

import project.dao.BarDao;
import project.model.Bar;
import project.model.Bars;
import project.rest.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
@Path("/bars")
public class BarResource {
    private BarDao barDao = new BarDao();

    // 获取所有bars
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Bars getAllBars() throws SQLException {
        Bars wrapper = new Bars();
        wrapper.setBars(barDao.getAllBars());
        return wrapper;
    }

    // 根据名称查询单个bar
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Bar getBarByName(@PathParam("name") String name) throws SQLException {
        return barDao.getBarByName(name); 
    }

    // 新增bar
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addBar(
        @FormParam("name") String name,
        @FormParam("weight") String weight,
        @FormParam("cals") int cals,
        @FormParam("manufacturer") String manufacturer
    ) throws SQLException {
        barDao.addBar(name, weight, cals, manufacturer);
        return Response.status(Response.Status.CREATED).build();
    }

    // 其他方法（PUT/DELETE）
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateBar(
        @PathParam("id") int id,
        @FormParam("newName") String newName
    ) throws SQLException {
        barDao.updateBar(id, newName);
        return Response.ok().build();
    }

    @PUT
    @Path("/byName")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateBarByName(
        @FormParam("currentName") String currentName,
        @FormParam("newName") String newName
    ) throws SQLException {
        barDao.updateBarByName(currentName, newName);
        return Response.ok().build();
    }

    // 删除巧克力棒
    @DELETE
    @Path("/{id}")
    public Response deleteBar(@PathParam("id") int id) throws SQLException {
        barDao.deleteBar(id);
        return Response.noContent().build();
    }
    
    @POST
    @Path("/fill")
    public Response fillTables() throws SQLException {
        barDao.fillTables(); 
        return Response.ok().build();
    }

    @POST
    @Path("/create")
    public Response createTables() throws SQLException {
        barDao.createTables(); 
        return Response.ok().build();
    }

    @DELETE
    @Path("/deleteAll")
    public Response deleteAllBars() throws SQLException {
        barDao.deleteAllBars(); 
        return Response.noContent().build();
    }
    
    
}