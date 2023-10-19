package com.devlop.siren.store.controller;

import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.store.domain.Store;
import com.devlop.siren.store.request.StoreRegisterRequest;
import com.devlop.siren.store.request.StoreUpdateRequest;
import com.devlop.siren.store.response.StoreResponse;
import com.devlop.siren.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;
    @PostMapping("/register/{role}")
    public ApiResponse<Boolean> registerStore(@PathVariable("role") String role, @RequestBody @Valid StoreRegisterRequest storeRegisterRequest){
        if(role.equals("") || role.length() <= 1){
            return ApiResponse.error(ResponseCode.ErrorCode.NOT_AUTH_ROLE);
        }else {
            boolean success = storeService.registerStore(storeRegisterRequest);
            return ApiResponse.ok(ResponseCode.Normal.CREATE, success);
        }
    }
    @GetMapping("/details/{storeId}")
    public ApiResponse<StoreResponse> detailsStore(@PathVariable("storeId") Long storeId){
        return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, storeService.detailsStore(storeId));
    }
    @GetMapping("/all")
    public ApiResponse<List<Store>> getAllStore(){
        return ApiResponse.ok(ResponseCode.Normal.RETRIEVE,storeService.getAllStores());
    }
    @GetMapping("/nearby")
    public ApiResponse<List<Store>> findNearbyStores(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radiusKm
    ) {
        return ApiResponse.ok(ResponseCode.Normal.RETRIEVE,storeService.getNearbyStores(latitude, longitude, radiusKm));
    }
    @PutMapping("/update/{storeId}")
    public ApiResponse<?> updateStore(@PathVariable("storeId") Long storeId ,@RequestBody StoreUpdateRequest storeUpdateRequest){
        storeService.updateStore(storeId,storeUpdateRequest);
        return ApiResponse.ok(ResponseCode.Normal.UPDATE, String.format("updateId > %d", storeId));
    }
    @DeleteMapping("/delete/{storeId}")
    public ApiResponse<?> deleteStore(@PathVariable("storeId") Long storeId)
    {
        Long deleteId = storeService.deleteStore(storeId);
        return ApiResponse.ok(ResponseCode.Normal.DELETE, String.format("삭제 된 ID > %d",deleteId));
    }

}
