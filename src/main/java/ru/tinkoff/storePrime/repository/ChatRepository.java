package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.storePrime.models.chat.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select chat from Chat chat where chat.seller.id = :senderId " +
            "or chat.customer.id = :senderId or chat.seller.id = :receiverId " +
            "or chat.customer.id = :receiverId")
    Optional<Chat> findChat(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Query("select chat from Chat chat where chat.seller.id = :accountId " +
            "or chat.customer.id = :accountId")
    List<Chat> findAllChats(@Param("accountId") Long accountId);



}
