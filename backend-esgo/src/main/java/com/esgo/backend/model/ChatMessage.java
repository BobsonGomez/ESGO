package com.esgo.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long recipientId;

    // To display names easily in the chat list
    private String senderName;
    private String recipientName;

    @Column(length = 2000)
    private String content;

    private LocalDateTime timestamp;
    private boolean isRead = false;
    private boolean isEdited = false;
}