package com.devlop.siren.domain.store.service;

import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.dto.request.StoreRegisterRequest;
import com.devlop.siren.domain.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.domain.store.dto.response.StoreResponse;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.domain.store.utils.GeocodingApi;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.google.maps.model.GeocodingResult;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StoreService {
  private static final double EARTH_RADIUS_KM = 6371.0;
  private final GeocodingApi geocodingApi;
  private final StoreRepository storeRepository;

  @Transactional
  public void registerStore(StoreRegisterRequest storeRegisterRequest) {
    try {
      GeocodingResult[] latLong = geocodingApi.geocodeAddress(storeRegisterRequest.getStreet());

      if (latLong != null && latLong.length > 0.0) {
        double latitude = latLong[0].geometry.location.lat;
        double longitude = latLong[0].geometry.location.lng;

        Store store = StoreRegisterRequest.from(storeRegisterRequest, latitude, longitude);
        storeRepository.save(store);
      } else {
        log.error("저장 실패");
        throw new GlobalException(ResponseCode.ErrorCode.FAIL_STORE_SAVE);
      }
    } catch (Exception e) {
      log.error("gecoding 파싱 중 에러 발생");
      throw new GlobalException(ResponseCode.ErrorCode.GEOCODING_PARSING_ERROR);
    }
  }

  @Transactional
  public void updateStore(
      Long storeId, StoreUpdateRequest storeUpdateRequest) {
    Store store =
        storeRepository
            .findByStoreId(storeId)
            .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_STORE));
    store.update(storeUpdateRequest);
  }

  @Transactional
  public Long deleteStore(Long storeId) {
    Store store =
        storeRepository
            .findByStoreId(storeId)
            .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_STORE));
    storeRepository.deleteByStoreId(store.getStoreId());

    return store.getStoreId();
  }

  public StoreResponse detailsStore(Long storeId) {
    if (!storeRepository.findByStoreId(storeId).isPresent()) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_STORE);
    } else {
      Store store = storeRepository.findByStoreId(storeId).get();
      return StoreResponse.from(store);
    }
  }

  public List<Store> getAllStores() {
    return storeRepository.findAll();
  }

  public List<Store> getNearbyStores(Double latitude, Double longitude, Double radiusKm) {
    List<Store> nearbyStores = new ArrayList<>();
    List<Store> allStores = storeRepository.findAll();

    for (Store store : allStores) {
      double storeLat = store.getLatitude();
      double storeLon = store.getLongitude();

      double distance = Haversine(latitude, longitude, storeLat, storeLon);

      if (distance <= radiusKm) {
        nearbyStores.add(store);
      }
    }
    return nearbyStores;
  }

  public double Haversine(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
  }

  public Store findStore(Long id) {
    return storeRepository
        .findByStoreId(id)
        .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_STORE));
  }
}
