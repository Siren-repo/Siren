package com.devlop.siren.store.controller;

import com.devlop.siren.store.dto.StoreRegisterDto;
import com.devlop.siren.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;
    @PostMapping("/register")
    public ResponseEntity<Boolean> registerStore(@RequestParam String role, @RequestBody StoreRegisterDto storeRegisterDto){
        return ResponseEntity.ok(storeService.registerStore(role,storeRegisterDto));
    }
    @PutMapping("/update/{storeId}")
    public void updateStore(@PathVariable Long storeId ,@RequestBody StoreUpdateRequest storeUpdateRequest){
        storeService.updateStore(storeId,storeUpdateRequest);
    }
}
