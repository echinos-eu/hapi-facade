package example.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import example.remoteAccess.PatientQuery;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

public class EncounterProvider implements IResourceProvider {

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Encounter.class;
  }

  @Search
  public List<Encounter> search(
      //Identifier = Fallnummer im KIS (FALLNR)
      @OptionalParam(name = Encounter.SP_IDENTIFIER) TokenParam identifier,
      @OptionalParam(name = Encounter.SP_PATIENT) ReferenceParam patient,
      @OptionalParam(name = Encounter.SP_DATE) DateParam date) {
    List<Encounter> encounter = new ArrayList<>();
    return encounter;
  }

  @Read
  public Encounter read(@IdParam IdType id) {
    Encounter encounter = new Encounter();
    //TODO: popoulate encounter
    return encounter;
  }

}
