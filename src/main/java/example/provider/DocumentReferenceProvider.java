package example.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import example.remoteAccess.PatientQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.DocumentReference.DocumentReferenceContentComponent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

public class DocumentReferenceProvider implements IResourceProvider {

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return DocumentReference.class;
  }

  @Read
  public DocumentReference read(@IdParam IdType id) {
    DocumentReference documentReference = new DocumentReference();
    //TODO: retrieve data & populate
    //TODO: set meta.profile to: https://gematik.de/fhir/isik/v3/Dokumentenaustausch/StructureDefinition/ISiKDokumentenMetadaten
    return documentReference;
  }

  @Search
  public List<DocumentReference> searchPatients(
      @OptionalParam(name = DocumentReference.SP_RES_ID) StringParam id,
      @OptionalParam(name = DocumentReference.SP_STATUS) TokenParam status,
      @OptionalParam(name = DocumentReference.SP_PATIENT) ReferenceParam patient,
      @OptionalParam(name = DocumentReference.SP_TYPE) TokenParam type,
      @OptionalParam(name = DocumentReference.SP_CATEGORY) TokenParam category,

      // this parameter is a SearchParameter from the IHD-MHD spec
      // searches on: DocumentReference.content.attachment.creation
      @OptionalParam(name = "creation") DateParam date,

      @OptionalParam(name = DocumentReference.SP_ENCOUNTER) ReferenceParam encounter) {
    List<DocumentReference> documentReferences = new ArrayList<>();
    //TODO: set meta.profile to: https://gematik.de/fhir/isik/v3/Dokumentenaustausch/StructureDefinition/ISiKDokumentenMetadaten
    return documentReferences;
  }

  /**
   * Simplified Push
   *
   * @param docRef
   * @return
   */
  @Create
  public MethodOutcome createDocument(@ResourceParam DocumentReference docRef) {
    byte[] data = docRef.getContentFirstRep().getAttachment().getData();
    if (data == null) {
      throw new UnprocessableEntityException(
          "DocumentReference.content.attachment.data has to be populated for Simplified Publish [ITI-105]");
    }
    //TODO: data persistieren
    //TODO: set DocumentReference.content.attachment.url to Binary url
    //TODO: DocRef Metadata mappen & persistieren

    //TODO:  Set ID of DocRef in MethodOutcome
    return new MethodOutcome();
  }

  @Operation(name = "$update-metadata", idempotent = false)
  public MethodOutcome patientInstanceOperation(
      @IdParam IdType docRefId,
      @OperationParam(name = "docStatus") CodeType theStart) {
    MethodOutcome outcome = new MethodOutcome();
    // TODO: get DocRef & update status
    throw new NotImplementedOperationException("$update-metadata not implemented yet");
  }
}
