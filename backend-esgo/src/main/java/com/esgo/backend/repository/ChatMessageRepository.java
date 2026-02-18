package com.esgo.backend.repository;

import com.esgo.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Get chat history between two users
    @Query("SELECT m FROM ChatMessage m WHERE (m.senderId = :userId1 AND m.recipientId = :userId2) OR (m.senderId = :userId2 AND m.recipientId = :userId1) ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(Long userId1, Long userId2);

    // Find all unique interactions for a user (either sent or received)
    @Query("SELECT DISTINCT m FROM ChatMessage m WHERE m.senderId = :userId OR m.recipientId = :userId ORDER BY m.timestamp DESC")
    List<ChatMessage> findUserConversations(Long userId);
}