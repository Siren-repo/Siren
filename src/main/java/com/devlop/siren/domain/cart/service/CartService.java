package com.devlop.siren.domain.cart.service;

import com.devlop.siren.domain.cart.dto.CartDto;
import com.devlop.siren.domain.cart.dto.ItemDto;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
  private static final String CART_KEY = "cart";
  private final ItemRepository itemRepository;
  private final RedisTemplate<String, ItemDto> cartRedisTemplate;
  private ListOperations<String, ItemDto> listOperations;

  @PostConstruct
  private void init() {
    listOperations = cartRedisTemplate.opsForList();
  }

  private String cartKeyGenerate(Long userId) {
    return CART_KEY + ":" + userId;
  }

  public CartDto add(ItemDto itemDto, UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    Item item = findItemById(itemDto.getItemId());
    ItemDto founded = getItemDtoByKeyAndValue(key, item);
    if (founded != null) {
      // 상품이 이미 장바구니에 존재하면 수량 증가
      int index = findIndex(key, founded);
      founded.setQuantity(founded.getQuantity() + itemDto.getQuantity());
      listOperations.set(key, index, founded);
      return new CartDto(key, List.of(founded));
    }
    // 상품이 장바구니에 없는 경우
    listOperations.rightPush(key, itemDto);
    return new CartDto(key, List.of(itemDto));
  }

  public CartDto retrieve(UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    return new CartDto(key, listOperations.range(key, 0, listOperations.size(key) - 1));
  }

  public void removeAll(UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    listOperations.trim(key, 1, 0);
  }

  public CartDto remove(Long itemId, UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    Item item = findItemById(itemId);
    ItemDto founded = getItemDtoByKeyAndValue(key, item);
    if(founded == null){
      throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM_IN_CART);
    }
    listOperations.remove(key, 1, founded);
    return new CartDto(key, listOperations.range(key, 0, listOperations.size(key) - 1));
  }

  public CartDto update(ItemDto itemDto, UserDetailsDto user){
    String key = cartKeyGenerate(user.getId());
    Item item = findItemById(itemDto.getItemId());
    ItemDto founded = getItemDtoByKeyAndValue(key, item);
    if(founded == null){
      throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM_IN_CART);
    }
    int index = findIndex(key, founded);
    founded.setQuantity(itemDto.getQuantity());
    listOperations.set(key, index, founded);
    return new CartDto(key, listOperations.range(key, 0, listOperations.size(key) - 1));
  }


  private int findIndex(String key, ItemDto itemDto){
    return listOperations.range(key, 0, listOperations.size(key) - 1).indexOf(itemDto);
  }

  private Item findItemById(Long itemId) {
    return itemRepository
            .findById(itemId)
            .orElseThrow(
                () -> {
                  throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
                });
  }


  private ItemDto getItemDtoByKeyAndValue(String key, Item item) {
    List<ItemDto> itemDtoList = listOperations.range(key, 0, listOperations.size(key) - 1);
    // 주어진 값과 일치하는 ItemDto 찾기
    for (ItemDto founded : itemDtoList) {
      if (item.getItemId().equals(founded.getItemId())) {
        return founded;
      }
    }
    // 일치하는 값이 없는 경우 null 반환
    return null;
  }
}
