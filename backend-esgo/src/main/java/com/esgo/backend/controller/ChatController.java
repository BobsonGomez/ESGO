package com.esgo.backend.controller;

import com.esgo.backend.model.ChatMessage;
import com.esgo.backend.model.User;
import com.esgo.backend.repository.ChatMessageRepository;
import com.esgo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatMessageRepository chatRepo;

    @Autowired
    private UserRepository userRepo;

    // 1. Send Message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(Principal principal, @RequestBody Map<String, Object> payload) {
        User sender = userRepo.findByUsername(principal.getName()).orElseThrow();
        Long recipientId = Long.valueOf(payload.get("recipientId").toString());
        String content = (String) payload.get("content");

        User recipient = userRepo.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found"));

        ChatMessage msg = new ChatMessage();
        msg.setSenderId(sender.getId());
        msg.setSenderName(sender.getFullname() != null ? sender.getFullname() : sender.getUsername());
        msg.setRecipientId(recipient.getId());
        msg.setRecipientName(recipient.getFullname() != null ? recipient.getFullname() : recipient.getUsername());
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());

        chatRepo.save(msg);
        return ResponseEntity.ok(msg);
    }

    // 2. Get Chat History with a specific user
    @GetMapping("/messages/{otherUserId}")
    public ResponseEntity<List<ChatMessage>> getMessages(Principal principal, @PathVariable Long otherUserId) {
        User me = userRepo.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(chatRepo.findChatHistory(me.getId(), otherUserId));
    }

    // 3. Get List of People I've chatted with (Contacts)
    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getConversations(Principal principal) {
        User me = userRepo.findByUsername(principal.getName()).orElseThrow();
        List<ChatMessage> allMessages = chatRepo.findUserConversations(me.getId());

        Map<Long, Map<String, Object>> uniqueContacts = new LinkedHashMap<>();

        for (ChatMessage msg : allMessages) {
            Long otherId;
            String otherName; // This is the display name stored in the message

            if (msg.getSenderId().equals(me.getId())) {
                otherId = msg.getRecipientId();
                otherName = msg.getRecipientName();
            } else {
                otherId = msg.getSenderId();
                otherName = msg.getSenderName();
            }

            if (!uniqueContacts.containsKey(otherId)) {
                Map<String, Object> contact = new HashMap<>();
                contact.put("userId", otherId);
                contact.put("name", otherName);
                contact.put("lastMessage", msg.getContent());
                contact.put("date", msg.getTimestamp());

                // FIX: Fetch the actual username from the User table for the profile picture
                userRepo.findById(otherId).ifPresent(user -> {
                    contact.put("username", user.getUsername());
                });

                uniqueContacts.put(otherId, contact);
            }
        }

        return ResponseEntity.ok(new ArrayList<>(uniqueContacts.values()));
    }

    // 4. Edit a Message
    @PutMapping("/edit/{messageId}")
    public ResponseEntity<?> editMessage(Principal principal, @PathVariable Long messageId, @RequestBody Map<String, String> payload) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        ChatMessage msg = chatRepo.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));

        // Security Check: Only sender can edit
        if (!msg.getSenderId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You can only edit your own messages.");
        }

        msg.setContent(payload.get("content"));
        msg.setEdited(true);
        chatRepo.save(msg);

        return ResponseEntity.ok("Message updated");
    }

    // 5. Delete a Message
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<?> deleteMessage(Principal principal, @PathVariable Long messageId) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        ChatMessage msg = chatRepo.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));

        // Security Check: Only sender can delete
        if (!msg.getSenderId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You can only delete your own messages.");
        }

        chatRepo.delete(msg);
        return ResponseEntity.ok("Message deleted");
    }
}