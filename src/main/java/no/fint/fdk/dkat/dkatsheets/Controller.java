package no.fint.fdk.dkat.dkatsheets;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Slf4j
@RestController
@Api(tags = "dcat-no-catalogs", description = "DCAT-NO Controller")
@CrossOrigin(origins = "*")
@RequestMapping(value = "/catalogs")
public class Controller {

    @Autowired
    private ModelService modelService;

    @ApiOperation(value = "Get Agent")
    @GetMapping(value = "", produces = {"text/turtle"})
    public ResponseEntity<String> getOrganisationAsTurtle() throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        return ResponseEntity.ok(RdfUtilities.getTurtleString(modelService.getOrganisationModel(catalog)));
    }

    @ApiOperation(value = "Get Agent")
    @GetMapping(value = "", produces = {"application/rdf+xml"})
    public ResponseEntity<String> getOrganisationAsXml() throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        return ResponseEntity.ok(RdfUtilities.getXmlString(modelService.getOrganisationModel(catalog)));
    }

    @ApiOperation(value = "Get Catalog")
    @GetMapping(value = "/catalog", produces = {"text/turtle"})
    public ResponseEntity<String> getCatalogAsTurtle() throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        return ResponseEntity.ok(RdfUtilities.getTurtleString(modelService.getCatalogModel(catalog, dataset)));
    }

    @ApiOperation(value = "Get Catalog")
    @GetMapping(value = "/catalog", produces = {"application/rdf+xml"})
    public ResponseEntity<String> getCatalogAsXml() throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        return ResponseEntity.ok(RdfUtilities.getXmlString(modelService.getCatalogModel(catalog, dataset)));
    }

    @ApiOperation(value = "Get dataset")
    @GetMapping(value = "/dataset/{id}", produces = {"text/turtle"})
    public ResponseEntity<String> getDatasetAsTurtle(@PathVariable final String id) throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        Optional<Model> datasetModel = modelService.getDatasetModel(catalog, dataset, id);

        if (datasetModel.isPresent()) {
            return ResponseEntity.ok(RdfUtilities.getTurtleString(datasetModel.get()));
        }

        return ResponseEntity.notFound().build();

    }

    @ApiOperation(value = "Get full FDK")
    @GetMapping(value = "/fdk", produces = {"text/turtle"})
    public ResponseEntity<String> getFdkAsTurtle() throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        return ResponseEntity.ok(RdfUtilities.getTurtleString(modelService.getFdkModel(catalog, dataset)));
    }

    @ApiOperation(value = "Get full FDK")
    @GetMapping(value = "/fdk", produces = {"application/rdf+xml"})
    public ResponseEntity<String> getFdkAsXml() throws IOException, GeneralSecurityException {
        String catalog = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getOrganisationAsTurtle()).build().toString();
        String dataset = MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(Controller.class).getDatasetAsTurtle("")).build().toString();
        return ResponseEntity.ok(RdfUtilities.getTurtleString(modelService.getFdkModel(catalog, dataset)));
    }


}
