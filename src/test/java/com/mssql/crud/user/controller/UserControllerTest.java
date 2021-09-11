package com.mssql.crud.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.mssql.crud.user.dto.UserDto;
import com.mssql.crud.user.exception.NotFoundException;
import com.mssql.crud.user.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	UserService userService;

	UserDto userDto;

	@BeforeEach
	void setUp() {
		userDto = new UserDto();
	}

	@Test
	void testIndex() {
		when(userService.list(any(Pageable.class))).thenReturn(Flux.just(userDto));
		StepVerifier.create(userController.index(0, 1, List.of("id"), "ASC")).expectNextCount(1).verifyComplete();
	}

	@Test
	void testGet() {
		when(userService.get(anyLong())).thenReturn(Mono.just(userDto));
		StepVerifier.create(userController.get(100)).assertNext(result -> result.equals(userDto)).verifyComplete();
	}

	@Test
	void testGetNotFoundException() {
		when(userService.get(anyLong())).thenReturn(Mono.error(new NotFoundException("Any error message")));
		StepVerifier.create(userController.get(100)).expectError(NotFoundException.class).verify();
	}

	@Test
	void testStore() {
		when(userService.store(any(UserDto.class))).thenReturn(Mono.just(userDto));
		StepVerifier.create(userController.create(userDto)).assertNext(result -> result.equals(userDto))
				.verifyComplete();
	}

	@Test
	void testUpdate() {
		when(userService.update(anyLong(), any(UserDto.class))).thenReturn(Mono.just(userDto));
		StepVerifier.create(userController.update(100, userDto)).assertNext(result -> result.equals(userDto))
				.verifyComplete();
	}

	@Test
	void testUpdateNotFoundException() {
		when(userService.update(anyLong(), any(UserDto.class)))
				.thenReturn(Mono.error(new NotFoundException("Any error message")));
		StepVerifier.create(userController.update(100, userDto)).expectError(NotFoundException.class).verify();
	}

	@Test
	void testDelete() {
		when(userService.delete(anyLong())).thenReturn(Mono.empty());
		StepVerifier.create(userController.destroy(100)).verifyComplete();
	}
}
