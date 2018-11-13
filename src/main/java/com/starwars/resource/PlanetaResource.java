package com.starwars.resource;

import com.codahale.metrics.annotation.Timed;
import com.starwars.application.binder.BindingResource;
import com.starwars.domain.Planeta;
import com.starwars.domain.PlanetaCompleto;
import com.starwars.domain.repository.PlanetaRepository;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.starwars.util.CacheUtil.getNumAparicoesPlaneta;

@Api(value = "planeta", description = "Serviços de gerenciamento de planetas starwars.")
@Path("/planeta")
@BindingResource
public class PlanetaResource {

    @Inject
    private PlanetaRepository planetaRepository;

    @GET
    @Timed
    @Path("/{_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera o planeta pelo id.", response = PlanetaCompleto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Id invalido ou falha no banco de dados."),
            @ApiResponse(code = 404, message = "Nenhum planeta encontrado para o id fornecido.") })
    public Response pickPlanetaById(@ApiParam(value = "_id planeta", required = true) @PathParam("_id") String _id) {

        if (!ObjectId.isValid(_id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O valor do ID informado é invalido.").build();
        }

        Optional<Planeta> document = planetaRepository.getById(_id);
        return document.map(planeta -> Response.ok(completaPlaneta(planeta)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Timed
    @Path("/procurar/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera o planeta pelo nome.", response = PlanetaCompleto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Nome invalido ou falha no banco de dados."),
            @ApiResponse(code = 404, message = "Nenhum planeta encontrado para o nome fornecido.") })
    public Response getPlanetasByNome(@ApiParam(value = "nome do planeta", required = true) @PathParam("nome") String nome) {

        Optional<Planeta> document = planetaRepository.getByNome(nome);
        return document.map(planeta -> Response.ok(completaPlaneta(planeta)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Timed
    @Path("/{_id}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Remove um planeta da lista", response = String.class)
    public Response removePlaneta(@ApiParam(value = "_id planeta", required = true) @PathParam("_id") String _id) {

        if (!ObjectId.isValid(_id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O valor do ID informado é invalido.").build();
        }

        boolean sucess = planetaRepository.remove(_id);
        if (sucess) {
            return Response.ok("Planeta removido com sucesso").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Não foi encontrado um planeta para o id fornecido").build();
        }

    }

    @GET
    @Timed
    @Path("/todos")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera  a lista de todos os planetas.", response = PlanetaCompleto.class, responseContainer = "List")
    public Response getAllPlanetas() {
        List<Planeta> documents = planetaRepository.getAll();
        if (documents.size() > 0) {
            return Response.ok(completaPlanetas(documents)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Timed
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Salvar novo planeta.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid Request"),
            @ApiResponse(code = 422, message = "Invalid planeta data")})
    public Response addPlaneta( @ApiParam(value = "json da novo planeta", required = true) Planeta planeta, @Context UriInfo uriInfo) {

        //se o planeta já existir, lança bad request
        if ( planetaRepository.getByNome(planeta.getNome()).isPresent()) {
            throw new NotAllowedException("O planete  " + planeta.getNome() + " já existe");
        }

        URI uri = uriInfo.getAbsolutePathBuilder().path("procurar").path(planeta.getNome()).build();
        boolean sucess = planetaRepository.save(planeta);
        if (sucess) {
            return Response.created(uri).build();
        } else {
            return Response.ok("Não foi possível salvar o planeta.").build();
        }
    }

    @PUT
    @Timed
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Alterar planeta.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Invalid Request")})
    public Response updatePlaneta( @ApiParam(value = "json da novo planeta", required = true) Planeta planeta) {
        boolean sucess = planetaRepository.update(planeta);
        if (sucess) {
            return Response.ok("Planeta atualizado com sucesso.").build();
        } else {
            return Response.ok("Não foi possível salvar o planeta.").build();
        }
    }

    private PlanetaCompleto completaPlaneta(Planeta pl) {
        return new PlanetaCompleto(pl.get_id(), pl.getNome(),
                pl.getClima(), pl.getTerreno(), getNumAparicoesPlaneta(pl.getNome()));
    }

    private List<PlanetaCompleto> completaPlanetas(List<Planeta> list) {
        return list.stream().map(this::completaPlaneta).collect(Collectors.toList());
    }
}
