package example.remoteAccess;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class PatientQuery {

  public List<Patient> searchPatient(String name) {
    HttpHeaders headers = getHttpHeadersWithAuth();
    JsonObject searchObject = new JsonObject();
    searchObject.add("NAME", name);
    RestTemplate restTemplate = new RestTemplate();
    String fooResourceUrl
        = "https://demo.avp8.de:4001/api/Patient/Search";

    HttpEntity<String> request =
        new HttpEntity<String>(searchObject.toString(), headers);
    String personResultAsJsonStr =
        restTemplate.postForObject(fooResourceUrl, request, String.class);
    JsonElement jsonElement = JsonParser.parseString(personResultAsJsonStr);
    JsonArray asJsonArray = jsonElement.getAsJsonArray();
    List<Patient> patients = new ArrayList<>();
    asJsonArray.forEach(j -> {
      Patient p = new Patient();
      com.google.gson.JsonObject jsonObject = j.getAsJsonObject();
      // NAME handling
      String nameConcatinated = jsonObject.get("NAME").getAsString();
      String[] splitName = nameConcatinated.split(",");
      String[] splitGiven = splitName[1].split(" ");
      HumanName humanName = p.addName();
      Arrays.asList(splitGiven).forEach(humanName::addGiven);
      humanName.setFamily(splitName[0]);
      p.setId(jsonObject.get("PLFD").getAsString());
      patients.add(p);
    });
    return patients;
  }

  private static HttpHeaders getHttpHeadersWithAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(
        "INSERT_TOKEN_HERE");
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
