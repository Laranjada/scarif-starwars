package com.starwars.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.starwars.application.binder.BindingResource;
import com.starwars.domain.persistente.Planeta;
import com.starwars.domain.repository.CollectionPlanetaRepository;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "planeta")
@Path("/planeta")
@BindingResource
public class PlanetaResource {

    @Inject
    private CollectionPlanetaRepository collectionPlanetaRepository;

    @GET
    @Timed
    @Path("/get/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera o planeta pelo nome.", response = Planeta.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Nome invalido ou falha no banco de dados."),
            @ApiResponse(code = 404, message = "Nenhum planeta encontrado para o nome fornecido.") })
    public Planeta pickPlaneta(@ApiParam(value = "nome do planeta", required = true) @PathParam("nome") String nome) {
        return collectionPlanetaRepository.getByNome(nome);
    }

    @GET
    @Timed
    @Path("/remove")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Remove um planeta da lista", response = String.class)
    public String removePlaneta() {
        collectionPlanetaRepository.remove();
        return "Last person remove. Total count: " + collectionPlanetaRepository.getCount();
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera  a lista de todos os planetas.", response = Planeta.class, responseContainer = "List")
    public List<Planeta> getAllPlanetas() {
        return collectionPlanetaRepository.getAll();
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Salvar novo planeta.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid Request")})
    public String addPlaneta( @ApiParam(value = "json da novo planeta", required = true) Planeta planeta) {
        return collectionPlanetaRepository.save(planeta);
    }
}
