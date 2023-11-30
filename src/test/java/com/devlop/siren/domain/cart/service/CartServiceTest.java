package com.devlop.siren.domain.cart.service;

import com.devlop.siren.domain.cart.dto.ItemDto;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @InjectMocks
    private CartService cartService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RedisTemplate<String, ItemDto> cartRedisTemplate;

    private final ItemDto validItemDto = new ItemDto(1L, 1L);
    private final ItemDto invalidItemDto = new ItemDto(null, null);
    private final User user = UserFixture.get("Test@Test", "12345678", "Test");
    private static final String CART_KEY = "Cart - UserId:";
    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(user, "id", 1L);
    }
    @Test
    @DisplayName("장바구니에 item을 추가할 수 있다.")
    void add(){

        when(itemRepository.findById(validItemDto.getItemId())).thenReturn(any());
        when(userRepository.findByEmail("Test")).thenReturn(any());
        assertThat(cartService.add(validItemDto, UserDetailsDto.fromEntity(user)).getCartIdentifier()).isEqualTo(CART_KEY + ":" + user.getId());
    }


}
