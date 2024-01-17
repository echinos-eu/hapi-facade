package example.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;

public class BinaryProvider implements IResourceProvider {

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Binary.class;
  }

  @Read
  public Binary read(@IdParam IdType id) {
    Binary binary = new Binary();
    //TODO: populate binary
    return binary;
  }
}
