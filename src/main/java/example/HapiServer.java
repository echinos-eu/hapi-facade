package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import example.provider.BinaryProvider;
import example.provider.DocumentReferenceProvider;
import example.provider.PatientProvider;
import example.validation.ISiKDocumentValidationChain;
import example.validation.RequestValidatingInterceptorExtended;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

@WebServlet("/*")
public class HapiServer extends RestfulServer {

  public HapiServer() {
    super(FhirContext.forR4());
  }

  @Override
  public void initialize() {

    // add IResourceProvider
    List<IResourceProvider> provider = new ArrayList<>();
    PatientProvider patientProvider = new PatientProvider();
    provider.add(patientProvider);
    DocumentReferenceProvider documentReferenceProvider = new DocumentReferenceProvider();
    provider.add(documentReferenceProvider);
    BinaryProvider binaryProvider = new BinaryProvider();
    provider.add(binaryProvider);
    setResourceProviders(provider);

    // InterceptorHandling
    registerInterceptor(new ResponseHighlighterInterceptor());
    this.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost:8080"));

    //create ISIK aware validation chain
    ISiKDocumentValidationChain iSiKDocumentValidationChain = new ISiKDocumentValidationChain(
        FhirContext.forR4Cached());
    CachingValidationSupport cachingValidationSupport = null;
    try {
      cachingValidationSupport = iSiKDocumentValidationChain.getValidationChain();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Create an interceptor to validate incoming requests
    RequestValidatingInterceptor requestInterceptor = new RequestValidatingInterceptorExtended();
    requestInterceptor.addValidatorModule(new FhirInstanceValidator(cachingValidationSupport));
    requestInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
    requestInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
    requestInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
    requestInterceptor.setResponseHeaderValueNoIssues("No issues detected");
    //registerInterceptor(requestInterceptor);

    // TODO: ResponseValidation deactivate in production
    // Create an interceptor to validate responses
    // This is configured in the same way as above
    ResponseValidatingInterceptor responseInterceptor = new ResponseValidatingInterceptor();
    responseInterceptor.addValidatorModule(new FhirInstanceValidator(cachingValidationSupport));
    responseInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
    responseInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
    responseInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
    responseInterceptor.setResponseHeaderValueNoIssues("No issues detected");
    //registerInterceptor(responseInterceptor);

  }


}
