package com.example.dldke.foodbox.CloudVision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.api.id3idutfky0i.TestMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VisionReturnActivity extends AppCompatActivity {

    private final String LOG_TAG = VisionReturnActivity.class.getSimpleName();
    private TestMobileHubClient apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_return);
        apiClient = new ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                .build(TestMobileHubClient.class);
        callCloudLogic("");
    }

    public void callCloudLogic(String message) {
        // Create components of api request
        final String method = "GET";

        final String path = "/items";
        final String body = "";
        final byte[] content = body.getBytes(StringUtils.UTF8);


        final Map parameters = new HashMap<>();
        parameters.put("lang", "en_US");
        parameters.put("FoodName", message);
        Log.d("FoodName",message);


        final Map headers = new HashMap<>();

        // Use components to create the api request
        ApiRequest localRequest =
                new ApiRequest(apiClient.getClass().getSimpleName())
                        .withPath(path)
                        .withHttpMethod(HttpMethodName.valueOf(method))
                        .withHeaders(headers)
                        .addHeader("Content-Type", "application/json")
                        .withParameters(parameters);

        // Only set body if it has content.
        if (body.length() > 0) {
            localRequest = localRequest
                    .addHeader("Content-Length", String.valueOf(content.length))
                    .withBody(content);
        }

        final ApiRequest request = localRequest;
        // Make network call on background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG,
                            "Invoking API w/ Request : " +
                                    request.getHttpMethod() + ":" +
                                    request.getPath());
                    Log.d(LOG_TAG,"test" + request);

                    final ApiResponse response = apiClient.execute(request);

                    final InputStream responseContentStream = response.getContent();
                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d(LOG_TAG, "Response : " + responseData);
                    }

                    Log.d(LOG_TAG, response.getStatusCode() + " " + response.getStatusText());

                } catch (final Exception exception) {
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}
