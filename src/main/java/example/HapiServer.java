package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import example.provider.PatientProvider;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

@WebServlet("/*")
public class HapiServer extends RestfulServer {

  public HapiServer() {
    super(FhirContext.forR4());
  }

  @Override
  public void initialize(){
    List<IResourceProvider> provider = new ArrayList<>();
    PatientProvider patientProvider = new PatientProvider();
    provider.add(patientProvider);
    setResourceProviders(provider);

    registerInterceptor(new ResponseHighlighterInterceptor());
    this.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost:8080"));

  }


}
