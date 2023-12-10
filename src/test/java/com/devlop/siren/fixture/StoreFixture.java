package com.devlop.siren.fixture;

import com.devlop.siren.domain.store.domain.Store;

import java.lang.reflect.Field;
import java.time.LocalTime;

public class StoreFixture {
  public static Store get(Long storeId) throws NoSuchFieldException, IllegalAccessException {
        Store store = Store.builder()
                .storeName("First Store Name")
                .storePhone("First Store Phone")
                .city("Seoul")
                .street("대전 서구 둔산중로32번길 29 1층 103호")
                .zipCode("54321")
                .openTime(LocalTime.of(18, 0))
                .closeTime(LocalTime.of(9, 0))
                .build();
        Field idField = Store.class.getDeclaredField("storeId");
        idField.setAccessible(true);
        idField.set(store, storeId);
        return store;
    }
}
