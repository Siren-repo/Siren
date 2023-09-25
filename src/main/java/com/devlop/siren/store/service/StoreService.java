package com.devlop.siren.store.service;

import com.devlop.siren.global.exception.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.store.utils.GeocodingApi;
import com.devlop.siren.store.domain.Store;
import com.devlop.siren.store.dto.StoreRegisterDto;
import com.devlop.siren.store.repository.StoreRepository;
import com.google.maps.model.GeocodingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final GeocodingApi geocodingApi;
    private final StoreRepository storeRepository;

    @Transactional
    public boolean registerStore(String role, StoreRegisterDto storeRegisterDto) {
        if (role.isEmpty()) {
            throw new GlobalException(ErrorCode.NOT_AUTH_ROLE);
        }

        try {
            GeocodingResult[] latLong = geocodingApi.geocodeAddress(storeRegisterDto.getStreet());

            if (latLong != null && latLong.length > 0) {
                double latitude = latLong[0].geometry.location.lat;
                double longitude = latLong[0].geometry.location.lng;

                log.error("위도: " + latitude);
                log.error("경도: " + longitude);

                Store store = StoreRegisterDto.from(storeRegisterDto, latitude, longitude);
                storeRepository.save(store);

                return true;
            } else {
                log.error("저장 실패");
                return false;
            }
        } catch (Exception e) {
            log.error("gecoding 파싱 중 에러 발생");
            throw new GlobalException(ErrorCode.GEOCODING_PARSING_ERROR);
        }
    }


    public void updateStore(Long storeId, StoreUpdateRequest storeUpdateRequest) {
        Store store = storeRepository.findByStoreId(storeId)
                .orElseThrow( () -> new GlobalException(ErrorCode.NOT_AUTH_ROLE));
        // 회원의 권한을 체크 해줄 Member(user) entity 필요
        store.update(storeUpdateRequest);
    }
}
