package com.devlop.siren.store.service;

import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.store.request.StoreUpdateRequest;
import com.devlop.siren.store.response.StoreResponse;
import com.devlop.siren.store.utils.GeocodingApi;
import com.devlop.siren.store.domain.Store;
import com.devlop.siren.store.request.StoreRegisterRequest;
import com.devlop.siren.store.repository.StoreRepository;
import com.google.maps.model.GeocodingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final GeocodingApi geocodingApi;
    private final StoreRepository storeRepository;
    private static final double EARTH_RADIUS_KM = 6371.0;
    @Transactional
    public boolean registerStore(StoreRegisterRequest storeRegisterRequest) {

        try {
            GeocodingResult[] latLong = geocodingApi.geocodeAddress(storeRegisterRequest.getStreet());

            if (latLong != null && latLong.length > 0) {
                double latitude = latLong[0].geometry.location.lat;
                double longitude = latLong[0].geometry.location.lng;

                log.info("위도: " + latitude);
                log.info("경도: " + longitude);

                Store store = StoreRegisterRequest.from(storeRegisterRequest, latitude, longitude);
                storeRepository.save(store);

                return true;
            } else {
                log.error("저장 실패");
                return false;
            }
        } catch (Exception e) {
            log.error("gecoding 파싱 중 에러 발생");
            return false;
        }
    }


    public void updateStore(Long storeId, StoreUpdateRequest storeUpdateRequest) {
        Store store = storeRepository.findByStoreId(storeId)
                .orElseThrow( () -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_STORE));
        // 회원의 권한을 체크 해줄 Member(user) entity 필요 함
        store.update(storeUpdateRequest);
    }

    @Transactional
    public Long deleteStore(Long storeId){
        storeRepository.deleteByStoreId(storeId);
        return storeId;
    }
    public StoreResponse detailsStore(Long storeId) {
        if(!storeRepository.findByStoreId(storeId).isPresent()){
            throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_STORE);
        }else{
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

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
