package no.fint.fdk.dkat.dkatsheets;

import no.fint.fdk.CatalogBuilder;
import no.fint.fdk.DatasetBuilder;
import no.fint.fdk.OrganisationBuilder;
import no.fint.fdk.OrganisationDataCatalogBuilder;
import org.apache.jena.rdf.model.Model;
import org.jooq.lambda.function.Function2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ModelService {

    @Value("${fint.fdk.dkat.organisasjonsnummer}")
    private String organisasjonsnummer;

    @Value("${fint.fdk.dkat.organisasjonsnavn}")
    private String organisasjonsnavn;

    @Autowired
    private SheetsRepository sheetsRepository;

    public Model getFdkModel(String uri, String dataset) throws IOException, GeneralSecurityException {
        Model organisationModel = getOrganisationModel(uri);
        Model catalogModel = getCatalogModel(uri, dataset);
        Map<String, Model> datasetModels = getDatasetModels(uri, dataset);


        OrganisationDataCatalogBuilder.OrganisationDataCatalogModelBuilder catalog = OrganisationDataCatalogBuilder.builder().dataCatalog()
                .organisation(organisationModel)
                .catalog(catalogModel);

        datasetModels.forEach((id, model) -> catalog.dataset(model));

        return catalog.build();
    }

    public Model getOrganisationModel(String uri) {

        return OrganisationBuilder.builder()
                .organisation(organisasjonsnummer, organisasjonsnavn, uri)
                .build();

    }

    public Model getCatalogModel(String uri, String dataset) throws IOException, GeneralSecurityException {

        Map<String, Model> datasetModels = getDatasetModels(uri, dataset);

        CatalogBuilder.CatalogResourceBuilder catalog = CatalogBuilder.builder()
                .organisation(uri);

        datasetModels.forEach((id, model) -> catalog.dataset(id));

        return catalog.build();
    }

    public Optional<Model> getDatasetModel(String uri, String dataset, String datasetId) throws IOException, GeneralSecurityException {
        Map<String, Model> datasetModels = getDatasetModels(uri, dataset);

        Optional<Map.Entry<String, Model>> ds = datasetModels.entrySet().stream().filter(entry -> entry.getKey().endsWith(datasetId)).findFirst();

        return ds.map(Map.Entry::getValue);

    }

    public Map<String, Model> getDatasetModels(String uri, String dataset) throws IOException, GeneralSecurityException {
        return sheetsRepository
                .getSheetValues()
                .stream()
                .collect(Collectors.toMap(
                        Function2.from(this::modelName).applyPartially(dataset),
                        Function2.from(this::model).apply(uri, dataset)));
    }

    private Function<List<Object>,Model> model(String uri, String dataset) {
        return (List<Object> row) -> DatasetBuilder.builder()
                .organisation(uri, modelName(dataset, row))
                .title(getCell(row, 1))
                .description(getCell(row, 2))
                .type(getCell(row, 3))
                .theme(getCell(row, 4))
                .accrualPeriodicity(getCell(row, 5))
                .spatial(getCell(row, 6))
                .accessRights(getCell(row, 7))
                .build();
    }

    private String modelName(String uri, List<Object> row) {
        return uri + getCell(row, 0);
    }

    private String getCell(List<Object> row, int index) {
        if (index >= row.size()) {
            return "";
        }
        Object cell = row.get(index);
        if (Objects.isNull(cell)) {
            return "";
        }
        return Objects.toString(cell);
    }

    /*
    DatasetBuilder.builder()
                .organisation(orgNum, "2")
                .title("Dataset2")
                .description("Dataset2 description")
                .theme(EuMetadataRegistry.DataTheme.ECON)
                .theme(EuMetadataRegistry.DataTheme.GOVE)
                .type("Data")
                .objective("Used for testing")
                .source("The universe")
                .accessRights(EuMetadataRegistry.AccessRight.NON_PUBLIC)
                .accrualPeriodicity(EuMetadataRegistry.Frequency.CONT)
                .spatial("https://data.geonorge.no/administrativeEnheter/fylke/doc/173156")
                .keyword("dog")
                .keyword("cat")
                .provenance("Vedtak")
                .legalBasisForRestriction(LegalBasis.of("lov", "lovtittel"))
                .legalBasisForAccess(LegalBasis.of("lov", "lovtittel"))
                .legalBasisForProcessing(LegalBasis.of("lov", "lovtittel"))
                .completeness("Always complete")
                .accuracy("Always accurat")
                .currentness("Always current")
                .build()
     */

}
