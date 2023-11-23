package com.devlop.siren.domain.cart.service;

import com.devlop.siren.domain.cart.dto.CartDto;
import com.devlop.siren.domain.cart.dto.ItemDto;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.item.service.ItemService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final String CART_KEY = "cart";
    private final ItemRepository itemRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, Long, Long> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    private String cartKeyGenerate(Long userId) {
        return CART_KEY + ":" + userId;
    }

    public CartDto addToCart(ItemDto itemDto, UserDetailsDto user) {
        String key = cartKeyGenerate(user.getId());
        Item item = findItemById(itemDto.getItemId());
        long newQuantity = 0;
        if (hashOperations.hasKey(key, item.getItemId())) {
            // 상품이 이미 장바구니에 존재하면 수량 증가
            long currentQuantity = hashOperations.get(key, item.getItemId());
            newQuantity = currentQuantity + itemDto.getQuantity();
            hashOperations.put(key, item.getItemId(), newQuantity);
        } else {
            // 상품이 장바구니에 없으면 새로 추가
            newQuantity = itemDto.getQuantity();
            hashOperations.put(key, item.getItemId(), newQuantity);
        }
        return new CartDto(key, itemDto.getItemId(), newQuantity);
    }

    public Map<Long, Long> getCart(UserDetailsDto user) {
        String key = cartKeyGenerate(user.getId());
        return hashOperations.entries(key);
    }

    public CartDto updateCart(UserDetailsDto user, ItemDto itemDto) {
        String key = cartKeyGenerate(user.getId());
        Item item = findItemById(itemDto.getItemId());
        hashOperations.put(key, item.getItemId(), itemDto.getQuantity());
        return new CartDto(key, item.getItemId(), itemDto.getQuantity());
    }

    public CartDto removeFromCart(UserDetailsDto user, ItemDto itemDto) {
        String key = cartKeyGenerate(user.getId());
        Item item = findItemById(itemDto.getItemId());
        hashOperations.delete(key, item.getItemId());
        return new CartDto(key, item.getItemId(), 0L);
    }

    private Item findItemById(Long itemId){
        return itemRepository.findById(itemId).orElseThrow(() -> {
            throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
        });
    }
}
