package at.rags.morpheus;

import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Work in progress.
 */
public class Morpheus {

  private MorpheusMapper mapper;

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public Morpheus() {
    mapper = new MorpheusMapper();
  }

  public JSONAPIObject jsonToObject(String jsonString) throws Exception {
    JSONAPIObject jsonapiObject = new JSONAPIObject();
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(jsonString);
    } catch (Exception e) {
      throw new Exception("Invalid JSON String.");
    }

    //included
    JSONArray includedArray = null;
    try {
      includedArray = jsonObject.getJSONArray("included");
      jsonapiObject.setIncluded(mapper.mapDataArray(includedArray, null));
    } catch (JSONException e) {
      Logger.debug("JSON does not contain included");
    }

    //data array
    JSONArray dataArray = null;
    try {
      dataArray = jsonObject.getJSONArray("data");
      jsonapiObject.setResources(mapper.mapDataArray(dataArray, jsonapiObject.getIncluded()));
    } catch (JSONException e) {
      Logger.debug("JSON does not contain data array");
    }

    //data object
    JSONObject dataObject = null;
    try {
      dataObject = jsonObject.getJSONObject("data");
      jsonapiObject.setResource(mapper.mapDataObject(dataObject, jsonapiObject.getIncluded()));
    } catch (JSONException e) {
      Logger.debug("JSON does not contain data object");
    }

    //TODO map included on relation
    //TODO map links
    //TODO errors
    //TODO meta

    return jsonapiObject;
  }
}
