package com.devlop.siren.store.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocodingApi {
    private final GeoApiContext context;

    public GeocodingApi(@Value("${google.geocoding.key}") String geoKey) {
        context = new GeoApiContext.Builder()
                .apiKey(geoKey)
                .build();
    }
    public GeocodingResult[] geocodeAddress(String address) throws IOException, InterruptedException, ApiException {
            return com.google.maps.GeocodingApi.geocode(context, address).await();
    }
}
