package example.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.HashMap;
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

    if (id.hasVersionIdPart()) {
      //VREAD
      return new Patient();
    }
    //VREAD
    else {
      //READ
      Patient patient = patients.get(id.getIdPart());
      return patient;
    }
  }

  @Search
  public ArrayList<Patient> searchPatients() {
    ArrayList<Patient> patientsList = new ArrayList<>(patients.values());
    return patientsList;
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
