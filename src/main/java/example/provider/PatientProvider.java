package example.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import example.remoteAccess.PatientQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

public class PatientProvider implements IResourceProvider {

  private HashMap<String, Patient> patients = new HashMap<>();

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Patient.class;
  }

  @Read
  public Patient read(@IdParam IdType id) {

    // VREAD is not mandatory in ISIK Dokumentenaustausch
//    if (id.hasVersionIdPart()) {
//      //VREAD
//      return new Patient();
//    }
    Patient patient = patients.get(id.getIdPart());
    return patient;
  }

  @Search
  public List<Patient> searchPatients(
      //Identifier = Patientennummer im KIS (PID)
      @OptionalParam(name = Patient.SP_IDENTIFIER) TokenParam identifier,
      @OptionalParam(name = Patient.SP_NAME) StringParam name,
      @OptionalParam(name = Patient.SP_BIRTHDATE) DateParam birthdate) {
    List<Patient> returnPatients = new ArrayList<>();
    PatientQuery patientQuery = new PatientQuery();
    List<Patient> patients = patientQuery.searchPatient(name.getValue());
    return patients;
  }

  @Create
  public MethodOutcome createPatient(@ResourceParam Patient patient) {
    String id = UUID.randomUUID().toString();
    patient.setId(id);
    patients.put(id, patient);

    MethodOutcome outcome = new MethodOutcome();
    outcome.setId(new IdType("Patient", id));
    return outcome;
  }

  @Update
  public MethodOutcome update(@IdParam IdType id, @ResourceParam Patient patient) {
    // TODO: Versionierung
    patients.put(id.getIdPart(), patient);
    return new MethodOutcome();
  }
}
