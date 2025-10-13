package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    JSONArray run(String url) throws BreedNotFoundException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getJSONArray("message");
        }catch (IOException e) {
            throw new BreedNotFoundException("temp");
        }
    }

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        try{
            ArrayList<String> subBreeds = new ArrayList<>();
            JSONArray jsonArray = this.run("https://dog.ceo/api/breed/"+breed+"/list");
            for(int i = 0; i < jsonArray.length(); i++){
                subBreeds.add(jsonArray.getString(i));
            }
            return subBreeds;
        }catch (Exception e){
            throw new BreedNotFoundException(breed);
        }
    }
}