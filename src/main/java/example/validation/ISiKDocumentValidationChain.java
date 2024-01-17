package example.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import java.io.IOException;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;

public class ISiKDocumentValidationChain {

  private final FhirContext ctx;

  public ISiKDocumentValidationChain(FhirContext context) {
    this.ctx = context;
  }

  public CachingValidationSupport getValidationChain() throws IOException {

    NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(ctx);
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/de.basisprofil.r4-1.4.0.tgz");
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/de.gematik.isik-basismodul-3.0.1.tgz");
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/de.gematik.isik-dokumentenaustausch-3.0.1.tgz");
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/dvmd.kdl.r4.2022-2022.1.2.tgz");
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/ihe.formatcode.fhir-1.1.0.tgz");
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/ihe.iti.balp-1.1.1.tgz");
    npmPackageSupport.loadPackageFromClasspath("classpath:packages/ihe.iti.mhd-4.2.0.tgz");

    ValidationSupportChain validationSupportChain = new ValidationSupportChain(
        npmPackageSupport,
        new DefaultProfileValidationSupport(ctx),
        new CommonCodeSystemsTerminologyService(ctx),
        new InMemoryTerminologyServerValidationSupport(ctx),
        new SnapshotGeneratingValidationSupport(ctx));
    CachingValidationSupport cachingValidationSupport = new CachingValidationSupport(
        validationSupportChain);
    return  cachingValidationSupport;
  }
}
