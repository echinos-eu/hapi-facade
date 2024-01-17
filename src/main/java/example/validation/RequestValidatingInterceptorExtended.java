package example.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.method.ResourceParameter;
import ca.uhn.fhir.validation.ValidationResult;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DocumentReference;

public class RequestValidatingInterceptorExtended extends RequestValidatingInterceptor {

  private static final org.slf4j.Logger ourLog =
      org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptorExtended.class);

  @Override
  public boolean incomingRequestPostProcessed(
      RequestDetails theRequestDetails, HttpServletRequest theRequest,
      HttpServletResponse theResponse) {

    EncodingEnum encoding = RestfulServerUtils.determineRequestEncodingNoDefault(theRequestDetails);
    if (encoding == null) {
      ourLog.trace("Incoming request does not appear to be FHIR, not going to validate");
      return true;
    }

    Charset charset = ResourceParameter.determineRequestCharset(theRequestDetails);
    String requestText = new String(theRequestDetails.loadRequestContents(), charset);

    if (isBlank(requestText)) {
      ourLog.trace("Incoming request does not have a body");
      return true;
    }

    // check//add meta.profile to DocRef resources
    FhirContext fhirContext = FhirContext.forR4Cached();
    IParser iParser = null;

    if (encoding == EncodingEnum.XML) {
      iParser = fhirContext.newXmlParser();
    } else if (encoding == EncodingEnum.JSON) {
      iParser = fhirContext.newJsonParser();
    }
    IBaseResource iBaseResource = iParser.parseResource(requestText);

    if (iBaseResource instanceof DocumentReference) {
      DocumentReference docref = (DocumentReference) iBaseResource;
      docref.getMeta().setProfile(new ArrayList<>());
      docref.getMeta().addProfile(
          "https://gematik.de/fhir/isik/v3/Dokumentenaustausch/StructureDefinition/ISiKDokumentenMetadaten");
      requestText = iParser.encodeResourceToString(docref);
    }
    ValidationResult validationResult = validate(requestText, theRequestDetails);

    if (super.isAddValidationResultsToResponseOperationOutcome()) {
      addValidationResultToRequestDetails(theRequestDetails, validationResult);
    }
    return true;
  }
}
