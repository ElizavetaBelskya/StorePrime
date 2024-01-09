package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.dto.ChatDto;
import ru.tinkoff.storePrime.dto.chat.ChatMessageDto;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController {


    @PreAuthorize("hasAnyAuthority('SELLER', 'CUSTOMER')")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody ChatMessageDto chatMessageDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long senderId = userDetailsImpl.getAccount().getId();
        Account.Role role = userDetailsImpl.getAccount().getRole();
        chatMessageDto.setSenderId(senderId);
        chatMessageDto.setSenderRole(role.toString());
        return ResponseEntity.ok(chatMessageDto);
    }

    @PreAuthorize("hasAnyAuthority('SELLER', 'CUSTOMER')")
    public ResponseEntity<List<ChatDto>> getAllChats(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return null;
    }


}
