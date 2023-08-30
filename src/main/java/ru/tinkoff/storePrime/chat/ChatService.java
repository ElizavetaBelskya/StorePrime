package ru.tinkoff.storePrime.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.ChatMessageConverter;
import ru.tinkoff.storePrime.dto.chat.ChatMessageDto;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.chat.Chat;
import ru.tinkoff.storePrime.models.chat.ChatMessage;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.ChatMessageRepository;
import ru.tinkoff.storePrime.repository.ChatRepository;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.CustomerService;
import ru.tinkoff.storePrime.services.SellerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatMessageRepository chatMessageRepository;

    private final ChatRepository chatRepository;

    private final SellerRepository sellerRepository;

    private final CustomerRepository customerRepository;
    
    public void saveMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = ChatMessageConverter.getChatMessageFromChatMessageDto(chatMessageDto);
        Optional<Chat> chat = chatRepository.findChat(chatMessage.getSenderId(), chatMessage.getReceiverId());
        Chat currentChat;
        if (chat.isPresent()) {
            currentChat = chat.get();
            currentChat.getMessageList().add(chatMessage);
        } else {
            List<ChatMessage> messageList = new ArrayList<>();
            messageList.add(chatMessage);
            currentChat = new Chat();
            if (chatMessageDto.getSenderRole().equals("SELLER")) {
                currentChat.setSeller(sellerRepository.findById(chatMessageDto.getSenderId()).orElseThrow(() -> new SellerNotFoundException("")));
                currentChat.setCustomer(customerRepository.findById(chatMessageDto.getReceiverId()).orElseThrow(() -> new CustomerNotFoundException("")));
            } else {
                currentChat.setSeller(sellerRepository.findById(chatMessageDto.getReceiverId()).orElseThrow(() -> new SellerNotFoundException("")));
                currentChat.setCustomer(customerRepository.findById(chatMessageDto.getSenderId()).orElseThrow(() -> new CustomerNotFoundException("")));
            }
            currentChat.setMessageList(messageList);
        }
        chatRepository.save(currentChat);
    }

    public List<Chat> getAllChats(Long accountId) {
        return chatRepository.findAllChats(accountId);
    }
    
}
